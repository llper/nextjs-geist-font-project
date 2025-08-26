# Time Tracker System - Sistema di Tracciamento Ore Dipendenti

Un sistema completo per il tracciamento delle ore di presenza e lavoro dei dipendenti, con gestione delle ferie e workflow di approvazione.

## 🏗️ Architettura

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0 con Java 17
- **Database**: PostgreSQL con Flyway per le migrazioni
- **Sicurezza**: JWT con integrazione Keycloak
- **API**: REST con documentazione automatica
- **Build**: Maven con Lombok per ridurre il boilerplate

### Frontend (Angular)
- **Framework**: Angular 17+ con TypeScript
- **UI**: Angular Material per componenti moderni
- **Autenticazione**: Keycloak Angular adapter
- **Styling**: SCSS con design responsive
- **Build**: Angular CLI con ottimizzazioni di produzione

### Deployment
- **Containerizzazione**: Docker per backend e frontend
- **Orchestrazione**: Kubernetes con Helm charts
- **Proxy**: Nginx per il frontend con configurazioni ottimizzate
- **Database**: PostgreSQL in cluster Kubernetes

## 🚀 Funzionalità Principali

### 👤 Gestione Utenti
- Autenticazione tramite Keycloak SSO
- Ruoli: EMPLOYEE, MANAGER, HR_MANAGER, ADMIN
- Profili utente con informazioni personali
- Gestione permessi granulari

### ⏰ Tracciamento Ore
- Registrazione ore di presenza (check-in/check-out)
- Tracciamento ore lavorate su progetti specifici
- Associazione ore a task e progetti
- Timer integrato per tracciamento in tempo reale
- Report ore giornaliere, settimanali, mensili

### 🏖️ Gestione Ferie
- Richiesta ferie con calendario integrato
- Workflow di approvazione configurabile
- Possibilità di saltare approvazione con label "approval skipped"
- Calcolo automatico giorni rimanenti
- Notifiche email per richieste e approvazioni

### 📊 Dashboard e Report
- Dashboard personalizzata per ruolo utente
- Grafici e statistiche ore lavorate
- Report esportabili (PDF, Excel)
- Analisi produttività per manager
- Overview progetti e task

### 🔧 Amministrazione
- Gestione dipendenti e ruoli
- Configurazione progetti e task
- Impostazioni sistema
- Audit log delle operazioni
- Backup e restore dati

## 🛠️ Setup e Installazione

### Prerequisiti
```bash
# Software richiesto
- Docker 20.10+
- Kubernetes 1.24+
- kubectl configurato
- Helm 3.8+
- Node.js 18+ (per sviluppo frontend)
- Java 17+ (per sviluppo backend)
- Maven 3.8+ (per build backend)
```

### 1. Clone del Repository
```bash
git clone <repository-url>
cd timetracker-system
```

### 2. Configurazione Keycloak
```bash
# Configurare Keycloak con:
# - Server URL: http://idp.localhost
# - Realm: tt-realm
# - Client ID: PEAX-agent
# - Client Secret: b1c84ea2-32f2-4229-a1c1-09e5c29363b6
```

### 3. Build delle Immagini Docker

#### Backend
```bash
cd timetracker-backend
mvn clean package -DskipTests
docker build -t timetracker-backend:latest .
```

#### Frontend
```bash
cd timetracker-frontend
npm install
npm run build:prod
docker build -t timetracker-frontend:latest .
```

### 4. Deploy su Kubernetes
```bash
# Creare namespace
kubectl apply -f kubernetes/namespace.yaml

# Deploy PostgreSQL
kubectl apply -f kubernetes/postgresql-deployment.yaml

# Deploy Backend
kubectl apply -f kubernetes/backend-deployment.yaml

# Deploy Frontend
kubectl apply -f kubernetes/frontend-deployment.yaml

# Configurare Ingress
kubectl apply -f kubernetes/ingress.yaml
```

### 5. Verifica Deployment
```bash
# Controllare status pods
kubectl get pods -n timetracker

# Controllare servizi
kubectl get services -n timetracker

# Controllare ingress
kubectl get ingress -n timetracker
```

## 🔧 Sviluppo Locale

### Backend Development
```bash
cd timetracker-backend

# Avviare PostgreSQL locale
docker run --name postgres-dev -e POSTGRES_DB=timetracker_dev \
  -e POSTGRES_USER=timetracker_dev -e POSTGRES_PASSWORD=timetracker_dev \
  -p 5432:5432 -d postgres:15

# Avviare applicazione
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### Frontend Development
```bash
cd timetracker-frontend

# Installare dipendenze
npm install

# Avviare dev server
ng serve --port 4200

# Build di produzione
ng build --configuration production
```

## 🧪 Testing

### Backend Testing
```bash
cd timetracker-backend

# Unit tests
mvn test

# Integration tests
mvn verify

# Test API con curl
curl -X GET http://localhost:8080/api/health \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Frontend Testing
```bash
cd timetracker-frontend

# Unit tests
ng test

# E2E tests
ng e2e

# Linting
ng lint
```

## 📡 API Endpoints

### Autenticazione
- `POST /api/auth/login` - Login utente
- `POST /api/auth/logout` - Logout utente
- `GET /api/auth/profile` - Profilo utente corrente

### Time Entries
- `GET /api/time-entries` - Lista time entries
- `POST /api/time-entries` - Crea time entry
- `PUT /api/time-entries/{id}` - Aggiorna time entry
- `DELETE /api/time-entries/{id}` - Elimina time entry

### Vacation Requests
- `GET /api/vacation-requests` - Lista richieste ferie
- `POST /api/vacation-requests` - Crea richiesta ferie
- `PATCH /api/vacation-requests/{id}/approve` - Approva richiesta
- `PATCH /api/vacation-requests/{id}/reject` - Rifiuta richiesta

### Projects & Tasks
- `GET /api/projects` - Lista progetti
- `POST /api/projects` - Crea progetto
- `GET /api/projects/{id}/tasks` - Task di un progetto
- `POST /api/tasks` - Crea task

### Admin
- `GET /api/admin/employees` - Lista dipendenti
- `POST /api/admin/employees` - Crea dipendente
- `GET /api/admin/reports` - Report amministrativi

## 🔒 Sicurezza

### Autenticazione
- JWT tokens con scadenza configurabile
- Refresh token automatico
- Logout sicuro con invalidazione token

### Autorizzazione
- Controllo ruoli a livello di endpoint
- Guards Angular per protezione route
- Middleware Spring Security per API

### Protezione Dati
- Crittografia password con BCrypt
- Validazione input lato client e server
- Sanitizzazione dati per prevenire XSS
- CORS configurato per domini autorizzati

## 📊 Monitoraggio

### Health Checks
- `GET /api/health` - Status applicazione backend
- `GET /health` - Status applicazione frontend
- Kubernetes liveness e readiness probes

### Logging
- Structured logging con Logback
- Log aggregation con ELK stack (opzionale)
- Audit trail per operazioni critiche

### Metriche
- Spring Boot Actuator endpoints
- Prometheus metrics (opzionale)
- Grafana dashboards (opzionale)

## 🚀 Deployment Produzione

### Configurazioni Ambiente
```yaml
# Backend environment variables
DB_HOST: postgresql-service
DB_PORT: 5432
DB_NAME: timetracker_prod
KEYCLOAK_HOST: idp.company.com
KEYCLOAK_REALM: tt-realm

# Frontend environment variables
API_URL: https://api.timetracker.company.com
KEYCLOAK_URL: https://idp.company.com
```

### SSL/TLS
```yaml
# Configurare certificati SSL
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  annotations:
    cert-manager.io/cluster-issuer: "letsencrypt-prod"
spec:
  tls:
  - hosts:
    - timetracker.company.com
    secretName: timetracker-tls
```

### Backup Database
```bash
# Backup automatico PostgreSQL
kubectl create cronjob postgres-backup \
  --image=postgres:15 \
  --schedule="0 2 * * *" \
  -- pg_dump -h postgresql-service -U timetracker timetracker_prod > backup.sql
```

## 🤝 Contribuire

### Workflow Sviluppo
1. Fork del repository
2. Creare feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit delle modifiche (`git commit -m 'Add AmazingFeature'`)
4. Push del branch (`git push origin feature/AmazingFeature`)
5. Aprire Pull Request

### Coding Standards
- **Backend**: Google Java Style Guide
- **Frontend**: Angular Style Guide
- **Database**: Naming conventions PostgreSQL
- **Git**: Conventional Commits

## 📝 Licenza

Questo progetto è rilasciato sotto licenza MIT. Vedere il file `LICENSE` per dettagli.

## 📞 Supporto

Per supporto tecnico o domande:
- 📧 Email: support@company.com
- 📖 Wiki: [Project Wiki](wiki-url)
- 🐛 Issues: [GitHub Issues](issues-url)

## 🔄 Changelog

### v1.0.0 (2024-01-XX)
- ✨ Implementazione iniziale sistema completo
- 🔐 Integrazione Keycloak per autenticazione
- 📊 Dashboard con grafici e statistiche
- 🏖️ Sistema gestione ferie con approvazioni
- ⏰ Tracciamento ore con timer integrato
- 🐳 Containerizzazione Docker completa
- ☸️ Deployment Kubernetes production-ready

---

**Sviluppato con ❤️ per una gestione efficiente del tempo lavorativo**
