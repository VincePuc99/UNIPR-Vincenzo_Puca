# Elfo.POC.Todo - README per main

Questo file e pensato come README di riferimento per il branch main.

## Panoramica

Applicazione full stack Todo composta da:
- Backend ASP.NET Core Web API
- Frontend Vue (stack del branch main)
- SQL Server in Docker

## Struttura

- Backend
- Frontend
- start-all.ps1
- stop-all.ps1

## Requisiti

- Windows + PowerShell
- .NET SDK
- Node.js
- Docker Desktop

## Avvio rapido

Dalla root del repository:

```powershell
./start-all.ps1
```

## Avvio manuale

### Backend

```powershell
cd Backend
dotnet run
```

### Frontend

```powershell
cd Frontend
npm install
npm run dev
```

## Configurazione DB

Verifica la connection string in Backend/appsettings.json e la password SQL usata dal container Docker.

## Stop ambiente

```powershell
./stop-all.ps1
```

## Nota

README.md nella branch feature/downgrade-vue2 contiene i dettagli specifici della migrazione a Vue 2.
