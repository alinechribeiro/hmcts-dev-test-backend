## HMCTS Task Manager Backend

This repository contains a small end‑to‑end task management system designed for HMCTS caseworkers. It is intentionally simple, but built with production‑style patterns. For that, it was integrated to the project tests, Docker, Postgres, React on frontend and JAVA with APIs on backend.

The project is split into two services:

- **Backend**: Java 21, Spring Boot 4.0.3, Gradle, Postgres for database. Repo: [https://github.com/alinechribeiro/hmcts-dev-test-backend](https://github.com/alinechribeiro/hmcts-dev-test-backend)
- **Frontend**: ReactJS, TypeScriptJS, Vite, TailwindCSS. Repo: [https://github.com/alinechribeiro/hmcts-dev-test-frontend](https://github.com/alinechribeiro/hmcts-dev-test-frontend)

---

### Main features on backend present on this repository

- **Task model**
  - Fields: `title`, optional `description`, `status`, `dueAt`
  - Status values: `TODO`, `IN_PROGRESS`, `DONE`, `BLOCKED`
- **REST API under `/api/tasks`**
  - `POST /api/tasks` – create task
  - `GET /api/tasks/{id}` – fetch task by id
  - `GET /api/tasks` – list all tasks
  - `PUT /api/tasks/{id}` – update title/description/due date
  - `PATCH /api/tasks/{id}/status` – update status only
  - `DELETE /api/tasks/{id}` – delete task
- **Validation & errors**
  - Bean Validation (`@NotBlank`, `@FutureOrPresent`, length limits)
  - Global exception handler returning clean JSON for 4xx cases
- **Persistence**
  - Postgres for local/dev runtime
  - H2 in‑memory database for tests

---

### Tech stack

- Java 21
- Spring Boot 4.0.3
- Postgres 16 for runtime
- H2 database for tests
- Gradle with wrapper

---

### Project structure

- `hmcts-task-backend/` with the following endpoints
  - `src/main/java/uk/gov/hmcts/tasks/model`
  - `src/main/java/uk/gov/hmcts/tasks/dto`
  - `src/main/java/uk/gov/hmcts/tasks/service`
  - `src/main/java/uk/gov/hmcts/tasks/web`
  - `src/test/java/uk/gov/hmcts/tasks`

---

### Prerequisites for running the backend

- Java 21
- Docker Desktop running for Postgres database

```bash
docker compose up -d db
```

This starts a Postgres 16 container, with:

- Database: `hmcts_tasks`
- User: `hmcts`
- Password: `hmcts`

**Run the Spring Boot app**

```bash
cd hmcts-task-backend
./gradlew bootRun
```

Then the API will be available at

- `http://localhost:8080/api/tasks`

---

### API overview

All the responses in JSON format coming from the following endpoints

`POST /api/tasks/` related to create task

`GET /api/tasks/{id}` related to get one task by id

`GET /api/tasks` to list all tasks

`PUT /api/tasks/{id}` related to updating a task by id

`PATCH /api/tasks/{id}/status`  for the update of a task status where the Body has the structure `{"status": "IN_PROGRESS"}`

`DELETE /api/tasks/{id}` for deletion of the task by id

---

### Testing

To run all backend tests:

```bash
./gradlew test
```

What is covered:

- **Service unit tests** using Mockito:
  - Creating tasks (initial status and fields)
  - Updating tasks (changing title/description/due date)
  - Deleting tasks (including not‑found behaviour)
- **Controller unit tests** with mocked services:
  - Mapping between HTTP layer and service layer for create, update and delete
- **Integration tests**:
  - `TaskControllerIntegrationTest` boots a Spring context with the `test` profile and uses H2 to verify that creating a task really persists data through the controller and repository.

---

### Notes

Other features like authentication, pagination, validation rules were ignored on that project to keep the focus on the simple goal of CRUD tasks.  
Other features I'd love to implement in another scope, despite the ones mentioned above, are the integration with third parties authentication with Google, LinkedIn and Github and an integration with OpenAI and Google Calendar to add the task to the calendar if the user is authenticated, but those features were out of the scope of that challenge. If you want to see them later, I will post it in another repository.