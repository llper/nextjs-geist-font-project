# Time Tracker System

Sistema completo per il tracciamento delle ore di presenza e lavoro dei dipendenti, con gestione delle richieste di ferie e workflow di approvazione.

## 🏗️ Architettura

- **Backend**: Spring Boot 3.2 con Java 17
- **Frontend**: Angular 18+ con TypeScript
- **Database**: PostgreSQL 15
- **Autenticazione**: JWT con Keycloak
- **Deployment**: Docker containers su Kubernetes

## 📋 Funzionalità

### Tracciamento Ore
- ✅ Registrazione ore di presenza
- ✅ Tracciamento ore lavorate su task specifici
- ✅ Associazione task a progetti aziendali
- ✅ Approvazione ore da parte dei manager
- ✅ Report giornalieri e settimanali

### Gestione Ferie
- ✅ Richiesta ferie con date e motivazione
- ✅ Workflow di approvazione basato sui ruoli
- ✅ Possibilità di saltare l'approvazione con label "Approval Skipped"
- ✅ Calendario ferie condiviso
- ✅ Calcolo giorni rimanenti

### Ruoli e Permessi
- **EMPLOYEE**: Può inserire ore e richiedere ferie
- **MANAGER**: Può approvare ore e ferie del proprio team
- **HR_MANAGER**: Può gestire tutte le ferie e accedere ai report
- **ADMIN**: Accesso completo al sistema

## 🚀 Setup e Installazione

### Prerequisiti
- Docker e Docker Compose
- Kubernetes cluster (per production)
- Keycloak server configurato

### Configurazione Keycloak
```
Server URL: http://idp.localhost
Realm: tt-realm
Client ID: PEAX-agent
Client Secret: b1c84ea2-32f2-4229-a1c1-09e5c29363b6
```

### Avvio con Docker Compose

1. **Backend**:
```bash
cd timetracker-backend
docker build -t timetracker-backend:latest .
```

2. **Database PostgreSQL**:
```bash
docker run -d \
  --name timetracker-postgres \
  -e POSTGRES_DB=timetracker \
  -e POSTGRES_USER=timetracker \
  -e POSTGRES_PASSWORD=timetracker \
  -p 5432:5432 \
  postgres:15-alpine
```

3. **Avvio Backend**:
```bash
docker run -d \
  --name timetracker-backend \
  --link timetracker-postgres:postgres \
  -e DB_HOST=postgres \
  -e DB_PORT=5432 \
  -e DB_NAME=timetracker \
  -e DB_USERNAME=timetracker \
  -e DB_PASSWORD=timetracker \
  -e KEYCLOAK_HOST=idp.localhost \
  -e KEYCLOAK_REALM=tt-realm \
  -e KEYCLOAK_CLIENT_ID=PEAX-agent \
  -e KEYCLOAK_CLIENT_SECRET=b1c84ea2-32f2-4229-a1c1-09e5c29363b6 \
  -p 8080:8080 \
  timetracker-backend:latest
```

### Deployment su Kubernetes

1. **Applicare i manifesti**:
```bash
kubectl apply -f kubernetes/namespace.yaml
kubectl apply -f kubernetes/postgresql-deployment.yaml
kubectl apply -f kubernetes/backend-deployment.yaml
kubectl apply -f kubernetes/frontend-deployment.yaml
kubectl apply -f kubernetes/ingress.yaml
```

2. **Verificare il deployment**:
```bash
kubectl get pods -n timetracker
kubectl get services -n timetracker
```

## 📊 API Endpoints

### Autenticazione
Tutte le API richiedono un token JWT valido nell'header:
```
Authorization: Bearer <JWT_TOKEN>
```

### Time Entries
```
GET    /api/time-entries/my              # Le mie ore
POST   /api/time-entries                 # Crea nuova entry
PUT    /api/time-entries/{id}            # Aggiorna entry
DELETE /api/time-entries/{id}            # Elimina entry
PATCH  /api/time-entries/{id}/approve    # Approva entry (Manager+)
GET    /api/time-entries/my/summary      # Riepilogo ore personali
GET    /api/time-entries/reports/daily   # Report giornaliero (Manager+)
```

### Vacation Requests
```
GET    /api/vacation-requests/my         # Le mie richieste ferie
POST   /api/vacation-requests            # Nuova richiesta ferie
PUT    /api/vacation-requests/{id}       # Aggiorna richiesta
PATCH  /api/vacation-requests/{id}/approve    # Approva (Manager+)
PATCH  /api/vacation-requests/{id}/reject     # Rifiuta (Manager+)
PATCH  /api/vacation-requests/{id}/skip-approval  # Salta approvazione (HR+)
GET    /api/vacation-requests/calendar   # Calendario ferie (Manager+)
```

### Employees
```
GET    /api/employees                    # Lista dipendenti (Manager+)
GET    /api/employees/me                 # I miei dati
PUT    /api/employees/me                 # Aggiorna i miei dati
```

## 🧪 Testing

### Test Backend con curl

1. **Health Check**:
```bash
curl http://localhost:8080/api/actuator/health
```

2. **Creare Time Entry** (con token JWT):
```bash
curl -X POST http://localhost:8080/api/time-entries \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "employeeId": 1,
    "type": "TASK",
    "entryDate": "2024-01-15T08:00:00",
    "startTime": "2024-01-15T09:00:00",
    "endTime": "2024-01-15T17:00:00",
    "hours": 8.0,
    "description": "Sviluppo nuova funzionalità",
    "taskId": 1
  }'
```

3. **Creare Richiesta Ferie**:
```bash
curl -X POST http://localhost:8080/api/vacation-requests \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "employeeId": 1,
    "startDate": "2024-03-15",
    "endDate": "2024-03-22",
    "type": "ANNUAL_LEAVE",
    "reason": "Vacanze primaverili"
  }'
```

## 🗄️ Struttura Database

### Tabelle Principali
- `employees` - Anagrafica dipendenti
- `projects` - Progetti aziendali
- `tasks` - Task associati ai progetti
- `time_entries` - Registrazioni ore lavorate
- `vacation_requests` - Richieste di ferie
- `project_employees` - Assegnazione dipendenti ai progetti

### Dati di Esempio
Il sistema include dati di esempio con:
- 6 dipendenti con ruoli diversi
- 4 progetti (E-Commerce, Mobile Banking, CRM, Legacy Migration)
- 8 task assegnati ai progetti
- Registrazioni ore di esempio
- Richieste ferie con diversi stati

## 🔧 Configurazione

### Variabili d'Ambiente Backend
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=timetracker
DB_USERNAME=timetracker
DB_PASSWORD=timetracker

# Keycloak
KEYCLOAK_HOST=idp.localhost
KEYCLOAK_REALM=tt-realm
KEYCLOAK_CLIENT_ID=PEAX-agent
KEYCLOAK_CLIENT_SECRET=b1c84ea2-32f2-4229-a1c1-09e5c29363b6

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:3000
```

### Profili Spring
- `development` - Per sviluppo locale
- `test` - Per test automatici
- `production` - Per ambiente di produzione

## 📝 Note di Sviluppo

### Backend
- Utilizza Lombok per ridurre il boilerplate
- Flyway per le migrazioni database
- Spring Security con JWT
- Validazione con Bean Validation
- Logging strutturato con Logback

### Frontend (Angular)
- Keycloak adapter per autenticazione
- Angular Material per UI components
- RxJS per gestione asincrona
- TypeScript strict mode
- Lazy loading dei moduli

### Sicurezza
- JWT tokens con scadenza
- CORS configurato
- Validazione input lato server
- Autorizzazione basata sui ruoli
- Audit trail delle operazioni

## 🐛 Troubleshooting

### Problemi Comuni

1. **Errore connessione database**:
   - Verificare che PostgreSQL sia in esecuzione
   - Controllare le credenziali di connessione

2. **Errore autenticazione Keycloak**:
   - Verificare che Keycloak sia raggiungibile
   - Controllare configurazione realm e client

3. **CORS errors**:
   - Verificare configurazione CORS_ALLOWED_ORIGINS
   - Controllare che il frontend sia servito dall'URL corretto

### Log Utili
```bash
# Backend logs
kubectl logs -f deployment/timetracker-backend -n timetracker

# Database logs
kubectl logs -f deployment/postgresql -n timetracker
```

## 🤝 Contribuire

1. Fork del repository
2. Creare feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit delle modifiche (`git commit -m 'Add some AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Aprire una Pull Request

## 📄 Licenza

Questo progetto è sotto licenza MIT - vedere il file [LICENSE](LICENSE) per i dettagli.
