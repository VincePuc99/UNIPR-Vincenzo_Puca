# Elfo.POC.Todo.Downgrade - Branch feature/downgrade-vue2

Questo README e specifico per la branch di downgrade frontend.
Per la versione documentazione pensata per main, vedi README.main.md.

Applicazione full stack Todo con:
- Backend ASP.NET Core Web API
- Frontend Vue 2 + Vuetify 2 (migrato da stack Vue 3)
- SQL Server in Docker

## Struttura progetto

- Backend: API REST, autenticazione JWT, accesso DB SQL Server
- Frontend: interfaccia web (login + dashboard todo)
- Script root:
  - start-all.ps1: avvio rapido ambiente locale
  - stop-all.ps1: stop processi e container

## Requisiti

- Windows + PowerShell
- .NET SDK (compatibile con il progetto Backend)
- Node.js (vedi engines in Frontend/package.json)
- Docker Desktop

## Porte usate

- Backend HTTP: http://localhost:5035
- Frontend Vite (dev): in genere http://localhost:5173
- SQL Server Docker: localhost,1433

## Avvio rapido (consigliato)

Dalla root del repo:

```powershell
./start-all.ps1
```

Lo script:
- avvia Docker Desktop se non attivo
- avvia/crea il container SQL (nome: sql1)
- apre backend e frontend in nuove shell PowerShell

## Avvio manuale

### 1) Database (Docker)

```powershell
docker start sql1
```

Se il container non esiste, creare con gli stessi parametri usati in start-all.ps1.

### 2) Backend

```powershell
cd Backend
dotnet run
```

### 3) Frontend

```powershell
cd Frontend
npm install
npm run dev
```

## Configurazione importante DB

Nel backend, la connection string e in:
- Backend/appsettings.json

Verificare che la password del container SQL combaci con la password in connection string.
Lo script start-all.ps1 crea il container con:
- MSSQL_SA_PASSWORD = Str0ngPassword!2026

Se la connection string usa un valore diverso, aggiornarla prima di avviare l app.

## API principali

Base URL: http://localhost:5035/api/todo

- POST /login: login e rilascio token JWT
- GET /verify: verifica token JWT
- GET /: lista todo abilitate (auth richiesta)
- GET /{id}: dettaglio todo (auth richiesta)
- POST /: crea todo (auth richiesta)
- PUT /{id}: aggiorna todo (auth richiesta)
- DELETE /{id}: soft delete todo (auth richiesta)

Header richiesto per endpoint protetti:
- Authorization: Bearer <token>

## Branch downgrade Vue 2

Branch di lavoro:
- feature/downgrade-vue2

Questa branch contiene la migrazione frontend a:
- vue 2.7
- vue-router 3
- vuetify 2
- vite + @vitejs/plugin-vue2

## Build frontend

```powershell
cd Frontend
npm run build
```

## Stop ambiente

Dalla root:

```powershell
./stop-all.ps1
```

## Note operative

- Se npm viene lanciato dalla root repo (non da Frontend) comparira errore ENOENT su package.json.
- Usare sempre la cartella Frontend per i comandi npm, oppure il prefisso:

```powershell
npm --prefix "D:\Repos\Elfo.POC.Todo.Downgrade\Frontend" run dev
```
