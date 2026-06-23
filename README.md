# Professional Networking Platform — Microservices Social Network

A LinkedIn-style professional networking platform built as a distributed microservices system using Java, Spring Boot, and Spring Cloud. The platform supports user profiles, posts, professional connections (graph-based), file uploads, and event-driven notifications.

## Overview

This project demonstrates a production-style microservices architecture with independent, horizontally-scalable services communicating both synchronously (REST / OpenFeign) and asynchronously (Apache Kafka). Each service owns its own data store, following the database-per-service pattern, and the system is fully containerized with Docker and deployable on Kubernetes / Google Kubernetes Engine (GKE).

## Architecture

```
                         ┌─────────────────┐
                         │   API Gateway    │
                         │ (Spring Cloud GW)│
                         └────────┬─────────┘
                                  │
        ┌───────────┬────────────┼────────────┬──────────────┐
        │            │            │            │              │
  ┌─────▼────┐  ┌────▼─────┐ ┌────▼──────┐ ┌───▼────────┐ ┌───▼─────────┐
  │  User    │  │  Post    │ │Connection │ │Notification│ │  Uploader   │
  │ Service  │  │ Service  │ │ Service   │ │  Service   │ │  Service    │
  └────┬─────┘  └────┬─────┘ └────┬──────┘ └────┬───────┘ └───┬─────────┘
       │             │            │             │             │
  ┌────▼────┐   ┌────▼────┐  ┌────▼────┐        │        ┌────▼────┐
  │PostgreSQL│   │PostgreSQL│  │  Neo4j  │        │        │Cloudinary│
  │(user-db) │   │(post-db) │  │(graph DB)│       │        │ (media)  │
  └─────────┘   └─────────┘  └─────────┘         │        └─────────┘
                     │                            │
                     └──────────► Apache Kafka ◄──┘
                            (event-driven notifications)
```

**Service discovery:**
- **Local development:** Netflix Eureka (`discover-server`) is used for dynamic service registration and discovery.
- **Kubernetes / GKE deployment:** Eureka is not required — Kubernetes' built-in DNS-based service discovery resolves services directly by their Service name (e.g. `user-service`, `posts-service`), and routing is handled via a GCE Ingress controller in front of the API Gateway.

## Services

| Service | Responsibility | Database |
|---|---|---|
| **api-gateway** | Single entry point, request routing, JWT authentication | – |
| **discover-server** | Eureka service registry (local dev only) | – |
| **user-service** | User profiles & account management | PostgreSQL |
| **posts-service** | Post creation, feed, Kafka producer for notifications | PostgreSQL |
| **connections-service** | Professional connections / network graph | Neo4j |
| **notification-service** | Async notifications via Kafka consumer | PostgreSQL |
| **uploader-service** | Media/file upload handling | Cloudinary |

## Tech Stack

- **Language & Framework:** Java, Spring Boot, Spring Cloud (Gateway, Eureka)
- **Inter-service communication:** REST, OpenFeign (sync) · Apache Kafka (async, event-driven)
- **Databases:** PostgreSQL (relational data), Neo4j (graph-based connections)
- **File Storage:** Cloudinary
- **Security:** JWT-based authentication
- **Containerization & Orchestration:** Docker, Kubernetes, Google Kubernetes Engine (GKE)
- **Build Tool:** Maven

## Design Patterns Used

- **API Gateway** — centralized routing, auth, and load balancing
- **Service Discovery** — Eureka (local) / Kubernetes DNS (cluster)
- **Proxy Pattern** — OpenFeign clients abstract inter-service calls
- **Repository Pattern** — Spring Data JPA & Spring Data Neo4j
- **Publish–Subscribe** — Kafka-based event-driven notifications
- **DTO / Mapper** — ModelMapper for clean boundary translation between layers
- **Strategy Pattern** — used for pluggable business rules where applicable
- **Dependency Injection** — Spring-managed beans throughout

## Repository Structure

```
/api-gateway
/discover-server
/user-service
/posts-service
/connections-service
/notification-service
/uploader-service
/k8s                  # Kubernetes manifests (Deployments, StatefulSets, Services, Ingress)
README.md
```

## Running Locally

1. Ensure PostgreSQL, Neo4j, and Kafka are running locally (or via Docker Compose).
2. Start the Eureka registry: run `discover-server`.
3. Start `api-gateway`.
4. Start the remaining services: `user-service`, `posts-service`, `connections-service`, `notification-service`, `uploader-service`.
5. Access the platform through the API Gateway's base URL.

Each service can be built independently:
```bash
mvn -f <service-folder>/pom.xml clean package
```

## Deploying on Kubernetes / GKE

Kubernetes manifests for every service, database (as StatefulSets), Kafka, and the Ingress controller are provided in the `/k8s` directory.

Apply in order — databases and Kafka first, then services, then Ingress:

```bash
kubectl apply -f k8s/user-db.yml
kubectl apply -f k8s/posts-db.yml
kubectl apply -f k8s/notification-db.yml
kubectl apply -f k8s/connections-db.yml
kubectl apply -f k8s/kafka.yml

kubectl apply -f k8s/user-service.yml
kubectl apply -f k8s/posts-service.yml
kubectl apply -f k8s/connections-service.yml
kubectl apply -f k8s/notification-service.yml
kubectl apply -f k8s/uploader-service.yml
kubectl apply -f k8s/api-gateway.yml

kubectl apply -f k8s/ingress.yml
```

Once the Ingress has provisioned an external IP, the platform is reachable through it.

## License

MIT
