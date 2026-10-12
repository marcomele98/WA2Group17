# WA2G17

A support system for product warranties. Cashiers register a warranty when a product is sold, customers open tickets on their warranties, managers assign the tickets to technical experts, and customers and experts solve them through a chat.

Backend in Kotlin with Spring Boot, frontend in React.

## Features

- Separate views for customers, cashiers, managers and experts.
- Chat on every ticket, with file attachments.
- Login and permissions managed with Keycloak.
- Monitoring with Prometheus, Loki, Tempo and Grafana.
- Integration tests on PostgreSQL with Testcontainers.

## Run it

Requirements: Docker, a JDK and Node.js.

Build the server image, then start the backend services from the repository
root:

```bash
cd server && ./gradlew jibDockerBuild && cd ..
docker compose up
```

Then start the web client:

```bash
cd client
npm install
npm start
```

| Service    | Address                 |
|------------|-------------------------|
| Web client | http://localhost:3001   |
| Server API | http://localhost:8081   |
| Keycloak   | http://localhost:8080   |
| Grafana    | http://localhost:3000   |
| Prometheus | http://localhost:9090   |

---

Developed as a group project for the Web Applications II course at Politecnico di Torino (2023).
