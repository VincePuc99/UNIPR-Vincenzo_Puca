param(
    [switch]$WhatIfMode
)

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
$backendPath = Join-Path $root "Backend"
$frontendPath = Join-Path $root "Frontend"
$containerName = "sql1"
$processTrackerPath = Join-Path $root ".runtime-processes.json"

Write-Host "==> Controllo Docker CLI..."

$dockerCmd = Get-Command docker -ErrorAction SilentlyContinue

if (-not $dockerCmd) {
    throw "Docker CLI non trovata. Installa Docker Desktop."
}

Write-Host "==> Verifica cartelle..."

if (-not (Test-Path $backendPath)) {
    throw "Cartella Backend non trovata"
}

if (-not (Test-Path $frontendPath)) {
    throw "Cartella Frontend non trovata"
}

function Stop-MatchingProcesses {
    param(
        [string]$PathMatch,
        [string]$CommandMatch,
        [string]$Label
    )

    $procs = Get-CimInstance Win32_Process | Where-Object {
        $_.CommandLine -and
        $_.CommandLine -like "*$PathMatch*" -and
        $_.CommandLine -like "*$CommandMatch*"
    }

    if (-not $procs) {
        # Some shells started with -WorkingDirectory don't include the folder path in CommandLine.
        $procs = Get-CimInstance Win32_Process | Where-Object {
            $_.CommandLine -and
            $_.CommandLine -like "*$CommandMatch*"
        }
    }

    if (-not $procs) {
        Write-Host "==> Nessun processo trovato per: $Label"
        return
    }

    foreach ($proc in $procs) {
        $msg = "PID $($proc.ProcessId) - $Label"
        if ($WhatIfMode) {
            Write-Host "[WHATIF] Stop $msg"
        } else {
            try {
                Stop-ProcessTree -ProcessId $proc.ProcessId -Label $Label
                Write-Host "==> Fermato $msg"
            } catch {
                Write-Warning "Impossibile fermare $msg : $($_.Exception.Message)"
            }
        }
    }
}

function Stop-ProcessTree {
    param(
        [int]$ProcessId,
        [string]$Label
    )

    # Kill process tree to ensure child dotnet/node processes are stopped too.
    $taskkillOut = & taskkill /PID $ProcessId /T /F 2>&1
    if ($LASTEXITCODE -ne 0) {
        $taskkillText = ($taskkillOut | Out-String).Trim()
        throw "taskkill fallito per PID $ProcessId ($Label): $taskkillText"
    }
}

function Stop-TrackedProcesses {
    if (-not (Test-Path $processTrackerPath)) {
        Write-Host "==> Nessun file PID trovato: $processTrackerPath"
        return
    }

    Write-Host "==> Arresto processi tracciati da start-all..."

    try {
        $tracked = Get-Content -Path $processTrackerPath -Raw | ConvertFrom-Json
    }
    catch {
        Write-Warning "Impossibile leggere $processTrackerPath : $($_.Exception.Message)"
        return
    }

    if (-not $tracked) {
        Write-Host "==> File PID vuoto"
        return
    }

    foreach ($item in @($tracked)) {
        if (-not $item.Pid) {
            continue
        }

        $proc = Get-CimInstance Win32_Process -Filter "ProcessId = $($item.Pid)" -ErrorAction SilentlyContinue

        if (-not $proc) {
            Write-Host "==> PID $($item.Pid) non attivo: $($item.Label)"
            continue
        }

        if ($item.CommandHint -and $proc.CommandLine -and ($proc.CommandLine -notlike "*$($item.CommandHint)*")) {
            Write-Warning "PID $($item.Pid) attivo ma comando inatteso, skip: $($item.Label)"
            continue
        }

        if ($WhatIfMode) {
            Write-Host "[WHATIF] Stop PID $($item.Pid) - $($item.Label)"
        }
        else {
            try {
                Stop-ProcessTree -ProcessId $item.Pid -Label $item.Label
                Write-Host "==> Fermato PID $($item.Pid) - $($item.Label)"
            }
            catch {
                Write-Warning "Impossibile fermare PID $($item.Pid) - $($item.Label) : $($_.Exception.Message)"
            }
        }
    }

    if (-not $WhatIfMode) {
        try {
            Remove-Item -Path $processTrackerPath -Force -ErrorAction Stop
            Write-Host "==> File PID rimosso"
        }
        catch {
            Write-Warning "Impossibile rimuovere $processTrackerPath : $($_.Exception.Message)"
        }
    }
}

Stop-TrackedProcesses

Write-Host '==> Arresto processi frontend (npm/vite)...'
Stop-MatchingProcesses -PathMatch $frontendPath -CommandMatch 'vite' -Label 'Frontend Vite'
Stop-MatchingProcesses -PathMatch $frontendPath -CommandMatch 'npm run dev' -Label 'Frontend npm run dev'

Write-Host '==> Arresto processi backend (dotnet run)...'
Stop-MatchingProcesses -PathMatch $backendPath -CommandMatch 'dotnet run' -Label 'Backend dotnet run'

Write-Host "==> Arresto container Docker: $containerName"
if ($WhatIfMode) {
    Write-Host "[WHATIF] docker stop $containerName"
} else {
    $running = docker ps --format '{{.Names}}' | Where-Object { $_ -eq $containerName }
    if ($running) {
        docker stop $containerName | Out-Null
        Write-Host "==> Container $containerName fermato"
    } else {
        Write-Host "==> Container $containerName già fermo o non presente"
    }
}

Write-Host ""
Write-Host "==> ARRESTO COMPLETATO"
Write-Host "SQL Server container: $containerName"
Write-Host "Backend e Frontend arrestati (se in esecuzione)"