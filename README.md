## About The Project

A simple Spring Boot-based bank app.

### Built With

- Java 21
- Spring Boot
- Spring Cloud (Gateway, Load Balancing, Circuit Breaker, Config Server)
- Thymeleaf
- PostgreSQL
- Hibernate
- Keycloak

### Prerequisites

- Java 21
- Gradle 8
- Docker

---

## Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/nikita11044/finclave.git
   cd finclave
   ```

2. **Start Docker containers**

   ```bash
   docker-compose up --build
   ```

   > 💡 You can customize the database credentials in external config repo: https://github.com/nikita11044/finclave-config.git`.

3. **Build the application**

   ```bash
   ./gradlew clean build
   ```

---

## Keycloak

The application uses Keycloak as OAuth provider

> ⚠️ **Note:**  
> You must manually create client in keycloak in advance for the application to function correctly.
> You can use example data from config server
