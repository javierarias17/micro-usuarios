<br />
<div align="center">
<h3 align="center">PRAGMA POWER-UP — micro-usuarios</h3>
  <p align="center">
    Microservice responsible for user management and authentication. Handles registration of owners, employees, and customers, and issues JWT tokens.
  </p>
</div>

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
* ![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)

### Service Dependencies

Communicates with the following microservices via Feign Client:
- **micro-plazoleta** — validates restaurant ownership during employee registration

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

* JDK 17 [https://jdk.java.net/17/](https://jdk.java.net/17/)
* Gradle [https://gradle.org/install/](https://gradle.org/install/)
* MySQL [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/)

### Recommended Tools
* IntelliJ Community [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
* Postman [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

### Environment Variables

Configure the following environment variables before running:

| Variable | Description |
|---|---|
| `SERVER_PORT` | Port on which the service runs |
| `SPRING_PROFILES_ACTIVE` | Active profile (e.g. `dev`) |
| `DB_URL` | MySQL JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for JWT signing and validation |
| `JWT_EXPIRATION` | Token expiration time in milliseconds |
| `PLAZOLETA_SERVICE_URL` | Base URL of micro-plazoleta |

### Installation

1. Clone the repo
2. Change directory
   ```sh
   cd micro-usuarios
   ```
3. Create a MySQL database
4. Set the required environment variables
5. Build and run
   ```sh
   ./gradlew bootRun
   ```

<!-- USAGE -->
## Usage

Once running, open the Swagger UI in your browser:

```
http://localhost:<SERVER_PORT>/swagger-ui/index.html
```

### API Endpoints

| Method | Path | Role | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | PUBLIC | Authenticate and obtain JWT token |
| `POST` | `/api/v1/user/owner` | ADMIN | Register a restaurant owner |
| `POST` | `/api/v1/user/employee` | OWNER | Register a restaurant employee |
| `POST` | `/api/v1/user/customer` | PUBLIC | Register a customer |
| `GET` | `/api/v1/user/{id}/is-owner` | INTERNAL | Check if a user is an owner |
| `GET` | `/api/v1/user/employee/{employeeId}/restaurant-id` | INTERNAL | Get the restaurant assigned to an employee |
| `GET` | `/api/v1/user/{id}/phone` | INTERNAL | Get a user's phone number |

<!-- TESTS -->
## Tests

```sh
./gradlew test jacocoTestReport
```

Or right-click the test folder in IntelliJ and choose **Run tests with coverage**.
