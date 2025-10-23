# Devsus Technical Test - Monorepo

This README explains how to bring up the entire project using Docker Compose. It uses the included `docker-compose.yml` at the repository root to start two microservices (`client-service` and `account-service`), a PostgreSQL database, Zookeeper and Kafka.

## What the compose brings up
- client-service (built from `./client-person-microservice`)
  - Container port 8080 mapped to host 8080
  - Environment (overrides in compose): database URL/credentials, Kafka bootstrap servers, CLIENT_SERVICE_URL
- account-service (built from `./account-movements-microservice`)
  - Container port 8080 mapped to host 8081
  - Environment: database URL/credentials, Kafka bootstrap servers
- postgres (postgres:14-alpine)
  - Exposes 5432
  - Data persisted to `~/apps/postgres` on host
- zookeeper (Confluent CP Zookeeper)
  - Exposes 2181 (mapped to host 22181)
- kafka (Confluent CP Kafka)
  - Exposes internal 9092 and host 29092

## Prerequisites
- Docker Desktop (Windows) or Docker Engine and Docker Compose v2.
- At least 2-4GB free memory for Docker to run the services comfortably.

## How to start everything
Open PowerShell and run the following from the repo root:

```powershell
# Start services in foreground
docker compose up --build

# Or start in background (detached)
docker compose up --build -d
```

To stop and remove containers, networks and volumes created by compose:

```powershell
docker compose down
```

## Environment variables (extracted from `docker-compose.yml`)
The compose declares environment variables for services. You can override these by providing an `.env` file in the repo root or by exporting env vars before running `docker compose`.

Service-level variables and meanings (defaults taken from `docker-compose.yml`):

- For `client-service` and `account-service`:
  - `SPRING_DATASOURCE_URL` (default: `jdbc:postgresql://postgres:5432/devdb`)
    - JDBC URL used by Spring Boot to connect to Postgres inside compose (service name `postgres`).
  - `SPRING_DATASOURCE_USERNAME` (default: `devuser`)
    - DB user.
  - `SPRING_DATASOURCE_PASSWORD` (default: `devpassword`)
    - DB password.
  - `KAFKA_BOOTSTRAP_SERVERS` (default: `kafka:9092`)
    - Kafka bootstrap URL pointing to the `kafka` service in compose.
  - `CLIENT_SERVICE_URL` (client-service only; default: `http://client-service:8080`)
    - Internal URL used by account-service or others to reach client-service. When running on host, change to `http://localhost:8080`.

- For `postgres` (environment used by the image):
  - `POSTGRES_USER` (default: `devuser`)
  - `POSTGRES_PASSWORD` (default: `devpassword`)
  - `POSTGRES_DB` (default: `devdb`)

- For `kafka` (selected important vars):
  - `KAFKA_BROKER_ID` (default: `1`)
  - `KAFKA_ZOOKEEPER_CONNECT` (default: `zookeeper:2181`)
  - `KAFKA_ADVERTISED_LISTENERS` (default: `PLAINTEXT://kafka:9092,PLAINTEXT_HOST://192.168.0.6:29092`)
    - Adjust `PLAINTEXT_HOST` to your host's IP if you need host access from outside Docker network.

## Example `.env` file
Create a `.env` file in the repo root to override compose variables and make development easier. Example:

```env
# Database
POSTGRES_USER=devuser
POSTGRES_PASSWORD=devpassword
POSTGRES_DB=devdb
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/devdb
SPRING_DATASOURCE_USERNAME=devuser
SPRING_DATASOURCE_PASSWORD=devpassword

# Kafka
KAFKA_BOOTSTRAP_SERVERS=kafka:9092

# Client service
CLIENT_SERVICE_URL=http://client-service:8080
```

## Accessing services from host
- client-service: http://localhost:8080
- account-service: http://localhost:8081
- Postgres: jdbc:postgresql://localhost:5432/devdb (host access)
- Kafka broker advertised host port: 29092 (mapped in compose)

If you need to connect to Kafka from the host using the advertised listener `PLAINTEXT_HOST://192.168.0.6:29092`, change the IP to your machine IP or `localhost` and update `KAFKA_ADVERTISED_LISTENERS` accordingly.

