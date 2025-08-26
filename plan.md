```markdown
# Detailed Implementation Plan for Employee Time Tracking Software

This plan outlines the creation of a full-stack solution with a new Angular frontend and a Spring Boot backend. The system will manage employee presence, task hours, and vacation requests (with an approval workflow that may mark requests as “approval skipped”). Both applications will be containerized and deployed on Kubernetes, with security driven by JWT tokens integrated with Keycloak (idp.localhost, realm tt-realm, client ID PEAX-agent, and provided secret).

---

## Backend (Spring Boot)

### 1. Project Setup
- **Create a New Spring Boot Project:**
  - Use [start.spring.io](https://start.spring.io) or your preferred IDE.
  - **Dependencies:** Spring Web, Spring Security, Spring Data JPA, PostgreSQL Driver, JWT (or Keycloak adapter).
- **Directory Structure:**
  - `/src/main/java/com/yourcompany/timetracker/`
    - `Application.java` – main entry point.
    - `/config/` – contains `SecurityConfig.java` (JWT validation and Keycloak integration).
    - `/controller/` – REST controllers: `TimeEntryController.java`, `VacationController.java`.
    - `/entity/` – JPA entities: `Employee.java`, `TimeEntry.java`, `VacationRequest.java` (add additional entities like `Project.java` or `Task.java` if needed).
    - `/repository/` – JPA repositories: `EmployeeRepository.java`, `TimeEntryRepository.java`, `VacationRequestRepository.java`.
    - `/service/` – service classes to encapsulate business logic.
    - `/exception/` – custom exceptions and `GlobalExceptionHandler.java`.

### 2. Application Configuration
- **File:** `src/main/resources/application.yml`
  - Configure PostgreSQL connection (URL, username, password).
  - Add Keycloak settings:
    - `keycloak.auth-server-url: http://idp.localhost/auth`
    - `keycloak.realm: tt-realm`
    - `keycloak.resource: PEAX-agent`
    - `keycloak.credentials.secret: b1c84ea2-32f2-4229-a1c1-09e5c29363b6`

### 3. Security & JWT Integration
- **File:** `SecurityConfig.java`
  - Configure Spring Security to validate JWT bearer tokens.
  - Use Keycloak’s JWKS endpoint for token validation.
  - Protect REST endpoints with appropriate authorization rules.
- **Error Handling:**
  - Create a custom authentication entry point for handling token errors.

### 4. Entity Models and Repositories
- **Employee.java:** Fields (id, name, role, email, etc.) with JPA annotations.
- **TimeEntry.java:** Fields (id, employee reference, startTime, endTime, type [“presence” or “task”]).
- **VacationRequest.java:** Fields (id, employee reference, startDate, endDate, approved flag, label).
- **Repositories:** Extend JpaRepository for each entity.

### 5. REST Controllers
- **TimeEntryController.java:**
  - `POST /api/time-entries` to create new time entries.
  - `GET /api/time-entries` to list entries.
- **VacationController.java:**
  - `POST /api/vacation-requests` to submit requests.
  - `PATCH /api/vacation-requests/{id}` to update status (set “approval skipped” if no explicit approval).
  - `GET /api/vacation-requests` for both employees and approvers.
- **Error Handling:** Use try-catch blocks and delegate errors to `GlobalExceptionHandler.java`.

### 6. Global Exception Handling
- **File:** `GlobalExceptionHandler.java`
  - Annotate with `@ControllerAdvice`.
  - Handle exceptions and return consistent HTTP error responses.

### 7. Containerization & Kubernetes for Backend
- **Dockerfile (in backend root):**
  - Use a multi-stage build (Maven build and then run with OpenJDK).
  - Expose port 8080.
- **Kubernetes YAML Files:**
  - `backend-deployment.yaml`: Define Deployment, set environment variables (DB, Keycloak).
  - `backend-service.yaml`: Define associated Service.

---

## Frontend (Angular)

### 1. Create a New Angular Project
- **Command:**  
  ```bash
  ng new time-tracker --routing --style=scss
  ```
- **Directory Structure:**
  - `/src/app/`
    - `app.module.ts` and `app-routing.module.ts`
    - `/components/` for UI components: Login, Dashboard, TimeEntryForm, VacationRequestForm.
    - `/services/`: `authentication.service.ts`, `api.service.ts`.
    - `/guards/`: `auth.guard.ts`.

### 2. Keycloak Integration
- **Library:** keycloak-angular.
- **Setup:**
  - Create a Keycloak initialization file (e.g., `keycloak-init.service.ts`) to configure using:
    - `url: 'http://idp.localhost/auth'`
    - `realm: 'tt-realm'`
    - `clientId: 'PEAX-agent'`
  - Initialize Keycloak in `app.module.ts` using `APP_INITIALIZER`.

### 3. UI Components & Modern Styling
- **Login Component:**
  - Simple form or redirection to Keycloak’s login page.
  - Display error messages with clear typography and spacing.
- **Dashboard Component:**
  - Show summaries of presence hours, task logs, and vacation statuses in tables/cards.
  - Use modern CSS (flexbox/grid) with custom SCSS; avoid any external icon libraries.
- **TimeEntryForm Component:**
  - Form fields: date pickers, time inputs.
  - Validate fields and display error state with styled messages.
- **VacationRequestForm Component:**
  - Fields: start date, end date.
  - UI displays a label “approval skipped” if the request bypasses approval.
  - Dynamic form validation and error messages.

### 4. Services & API Integration
- **ApiService:**  
  - Use Angular HttpClient to interact with backend REST endpoints.
  - Inject Keycloak JWT in the Authorization header.
  - Implement RxJS error handling using catchError.
- **AuthGuard:**
  - Protect routes requiring authenticated access.
  - Redirect unauthenticated users to login.

### 5. Containerization & Kubernetes for Frontend
- **Dockerfile (in Angular project root):**
  - Multi-stage build using Node for compilation and then serving with Nginx.
  - Expose port 80.
- **Kubernetes YAML Files:**
  - `angular-deployment.yaml`: Deployment configuration with environment variables (e.g., API base URL).
  - `angular-service.yaml`: Service file for frontend.

---

## Testing & Deployment

- **Backend Testing:**
  - Use curl commands such as:
    ```bash
    curl -X POST http://<backend-ip>:8080/api/time-entries \
         -H "Content-Type: application/json" \
         -H "Authorization: Bearer <JWT_TOKEN>" \
         -d '{"employeeId":1,"startTime":"2023-10-07T08:00:00Z","endTime":"2023-10-07T16:00:00Z", "type":"presence"}'
    ```
  - Test vacation endpoints similarly.
- **Frontend Testing:**
  - Validate login flow, API communication, and UI error handling.
- **Deployment:**
  - Build Docker images for both apps.
  - Deploy via Kubernetes using the provided YAML files.
  - Ensure PostgreSQL is available and reachable by configuring environment variables.

---

## Summary

- Created new Angular and Spring Boot projects from scratch.
- The backend provides REST endpoints for time and vacation tracking with JWT security (Keycloak integration) and PostgreSQL.
- Entities, repositories, and controllers are implemented with global exception handling.
- The Angular frontend features secure routing with Keycloak, modern UI components (login, dashboard, forms), and services for API interaction.
- Both applications are containerized (Docker) and deployed on Kubernetes.
- End-to-end testing is performed using curl commands on backend endpoints.
