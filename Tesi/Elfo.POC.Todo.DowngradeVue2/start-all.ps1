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

Write-Host "==> Verifico Docker Engine..."

$dockerReady = $false

try {
    docker info *> $null
    $dockerReady = $true
}
catch {
    $dockerReady = $false
}

if (-not $dockerReady) {

    Write-Host "Docker Engine non attivo. Avvio Docker Desktop..."

    $dockerDesktopPaths = @(
        "C:\Program Files\Docker\Docker\Docker Desktop.exe",
        "C:\Program Files (x86)\Docker\Docker\Docker Desktop.exe"
    )

    $dockerDesktop = $dockerDesktopPaths | Where-Object { Test-Path $_ } | Select-Object -First 1

    if (-not $dockerDesktop) {
        throw "Docker Desktop non trovato."
    }

    Start-Process $dockerDesktop

    Write-Host "Attendo Docker Engine..."

    for ($i=0; $i -lt 30; $i++) {

        Start-Sleep 3

        try {
            docker info *> $null
            $dockerReady = $true
            break
        }
        catch {
            $dockerReady = $false
        }
    }

    if (-not $dockerReady) {
        throw "Docker Engine non è partito."
    }

    Write-Host "Docker Engine pronto."
}

Write-Host "==> Controllo container SQL..."

$existing = docker ps -a --format "{{.Names}}" | Select-String "^$containerName$"

if ($existing) {

    $running = docker ps --format "{{.Names}}" | Select-String "^$containerName$"

    if ($running) {

        Write-Host "Container già attivo"

    } else {

        Write-Host "Avvio container esistente"
        docker start $containerName | Out-Null
    }

}
else {

    Write-Host "Creo container SQL Server..."

    docker run `
        -e ACCEPT_EULA=Y `
        -e MSSQL_SA_PASSWORD="Str0ngPassword!2026" `
        -e MSSQL_PID=Developer `
        -p 1433:1433 `
        -v mssql_data:/var/opt/mssql `
        --name $containerName `
        -d mcr.microsoft.com/mssql/server:2025-latest | Out-Null
}

Write-Host "==> Attendo SQL Server..."

$sqlReady = $false

for ($i=0; $i -lt 30; $i++) {

    try {

        $logs = docker logs $containerName 2>&1

        if ($logs -match "ready for client connections") {
            $sqlReady = $true
            break
        }

    } catch {}

    Start-Sleep 2
}

if ($sqlReady) {
    Write-Host "SQL Server pronto"
}
else {
    Write-Host "SQL potrebbe non essere ancora pronto"
}

Write-Host "==> Verifica cartelle..."

if (-not (Test-Path $backendPath)) {
    throw "Cartella Backend non trovata"
}

if (-not (Test-Path $frontendPath)) {
    throw "Cartella Frontend non trovata"
}

Write-Host "==> Avvio backend .NET..."

$backendProc = Start-Process powershell `
    -WorkingDirectory $backendPath `
    -ArgumentList "-NoExit","-Command","dotnet run" `
    -PassThru

Write-Host "==> Avvio frontend Vue..."

$frontendProc = Start-Process powershell `
    -WorkingDirectory $frontendPath `
    -ArgumentList "-NoExit","-Command","if (!(Test-Path node_modules)) { npm install }; npm run dev" `
    -PassThru

$trackedProcesses = @(
    @{
        Label = "Backend dotnet run"
        Pid = $backendProc.Id
        CommandHint = "dotnet run"
    },
    @{
        Label = "Frontend npm run dev"
        Pid = $frontendProc.Id
        CommandHint = "npm run dev"
    }
)

try {
    $trackedProcesses | ConvertTo-Json | Set-Content -Path $processTrackerPath -Encoding UTF8
    Write-Host "==> PID salvati in $processTrackerPath"
}
catch {
    Write-Warning "Impossibile salvare i PID in $processTrackerPath : $($_.Exception.Message)"
}

Write-Host ""
Write-Host "==> AVVIO COMPLETATO"
Write-Host "SQL Server container: $containerName"
Write-Host "Backend e Frontend aperti in nuove finestre"