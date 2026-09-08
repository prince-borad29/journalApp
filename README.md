 # 📔 Journal App

 A secure **RESTful Journal Management API** built with **Java and Spring Boot** that allows users to create, view, update, and delete personal journal entries.

 The application follows a layered architecture with dedicated **Controller, Service, Repository, DTO, Entity, Security, Cache, and Scheduler** components. It uses **MongoDB** for persistence, **Redis** for caching, **JWT-based authentication** for API security, and **Google OAuth2** for social authentication.

---

 ## 🚀 Features

 - 🔐 **JWT Authentication** for secure API access
- 🔑 **Google OAuth2 Authentication**
- 📝 Create, read, update, and delete journal entries
- 👤 User registration and user management
- 🔒 User-specific journal access
- 🗄️ MongoDB for journal and user data persistence
- ⚡ Redis caching for improved performance
- 📧 Email service for automated notifications
- ⏰ Scheduled email reminders using Spring Scheduler
- 📊 Sentiment analysis support for journal entries
- 📖 Swagger/OpenAPI API documentation
- 🧪 Unit and integration testing
- 🔍 SonarQube/SonarCloud integration for code quality analysis
- 📝 Application logging using Logback

---

 ## 🛠️ Tech Stack

 | Technology | Purpose |
| --- | --- |
| **Java 8** | Programming Language |
| **Spring Boot 2.7.15** | Backend Framework |
| **Spring Web** | REST API Development |
| **Spring Security** | Authentication & Authorization |
| **JWT** | Token-Based Authentication |
| **Google OAuth2** | Social Authentication |
| **MongoDB** | Database |
| **Redis** | Caching |
| **Spring Mail** | Email Notifications |
| **Spring Scheduler** | Automated Scheduled Tasks |
| **Swagger / OpenAPI** | API Documentation |
| **Lombok** | Boilerplate Code Reduction |
| **JUnit / Spring Boot Test** | Testing |
| **SonarQube / SonarCloud** | Code Quality Analysis |
| **Maven** | Build & Dependency Management |

 The project's Maven configuration confirms Spring Web, MongoDB, Security, Mail, Redis, JWT, SpringDoc OpenAPI, Lombok, and Sonar Maven integration. 

---

 ## 🏗️ Architecture

 The application follows a **layered architecture** that separates responsibilities across different components.

```
                    ┌─────────────────────┐
                    │      Client         │
                    │  Web / REST Client  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Controllers     │
                    │   REST API Layer    │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Services       │
                    │  Business Logic     │
                    └───────┬─────┬───────┘
                            │     │
                 ┌──────────┘     └──────────┐
                 ▼                           ▼
        ┌─────────────────┐          ┌─────────────────┐
        │   Repository    │          │      Redis      │
        │     Layer       │          │     Cache       │
        └────────┬────────┘          └─────────────────┘
                 │
                 ▼
        ┌─────────────────┐
        │     MongoDB     │
        │    Database     │
        └─────────────────┘
```

 The source tree is organized into packages including `controller`, `service`, `repository`, `entity`, `dto`, `model`, `filter`, `scheduler`, `cache`, `Config`, and `utils`.

---

 ## 📂 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── net/engineeringdigest/journalApp/
│   │       ├── Config/
│   │       │   ├── RedisConfig.java
│   │       │   ├── SpringSecurity.java
│   │       │   └── SwaggerConfig.java
│   │       │
│   │       ├── cache/
│   │       │
│   │       ├── constants/
│   │       │
│   │       ├── controller/
│   │       │   ├── AdminController.java
│   │       │   ├── GoogleAuthController.java
│   │       │   ├── JournalEntryController.java
│   │       │   ├── PublicController.java
│   │       │   └── UserController.java
│   │       │
│   │       ├── dto/
│   │       ├── entity/
│   │       ├── enums/
│   │       ├── filter/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── scheduler/
│   │       ├── service/
│   │       │   ├── EmailService.java
│   │       │   ├── JournalEntryService.java
│   │       │   ├── RedisService.java
│   │       │   ├── SentimentConsumerAnalysisService.java
│   │       │   ├── UserDetailsServiceImpl.java
│   │       │   └── UserService.java
│   │       ├── utils/
│   │       └── JournalApplication.java
│   │
│   └── resources/
│       └── logback.xml
│
└── test/
    └── java/
        └── net/engineeringdigest/journalApp/
            ├── repository/
            ├── scheduler/
            └── services/
```

 The repository contains dedicated controllers for journal entries, users, administration, public endpoints, and Google authentication.

---

 ## 📝 Journal Management

 Authenticated users can manage their own journal entries.

 ### Supported Operations

 | Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/journal` | Get current user's journal entries |
| `POST` | `/journal` | Create a journal entry |
| `GET` | `/journal/id/{id}` | Get a journal entry |
| `PUT` | `/journal/id/{id}` | Update a journal entry |
| `DELETE` | `/journal/id/{id}` | Delete a journal entry |

 The journal controller uses the authenticated user's identity to ensure that journal operations are performed against the user's own entries.

---

 ## 🗄️ Database

 The application uses **MongoDB** as its primary database through Spring Data MongoDB.

 MongoDB stores:

 - User information
- Journal entries
- User-to-journal relationships
- Journal metadata

 Spring Data MongoDB is configured as a project dependency in the Maven configuration.

---

 ## ⚡ Redis Caching

 **Redis** is integrated into the application to reduce repeated database access and improve response performance.

 The project contains:

 - `RedisConfig`
- `RedisService`
- Redis-related tests

 This provides a dedicated caching layer alongside MongoDB.

---

 ## 📧 Email Notifications

 The application integrates **Spring Mail** for sending emails.

 The email functionality is separated into an `EmailService`, while scheduled operations are handled through the scheduler package.

---

 ## ⏰ Scheduled Tasks

 The application includes a scheduler component for performing automated background tasks.

```
Spring Scheduler
       │
       ▼
 UserScheduler
       │
       ▼
 Check Users / Reminders
       │
       ▼
 Email Service
       │
       ▼
 Email Notification
```

 The project contains a dedicated `UserScheduler` and corresponding `UserSchedulerTest`.
---

 ## 📊 Sentiment Analysis

 Journal entries can include sentiment information, and the project contains a dedicated `SentimentConsumerAnalysisService` for sentiment-related processing.

 This provides the foundation for analyzing the emotional context of journal entries.

---

 ## 📖 API Documentation

 The project uses **SpringDoc OpenAPI / Swagger UI** for interactive API documentation.

 After starting the application, Swagger UI can be accessed at:

```
http://localhost:8080/swagger-ui.html
```

 The project includes `SwaggerConfig.java` and the `springdoc-openapi-ui` dependency.

---

 ## 🧪 Testing

 The project contains automated tests covering multiple application components.

 Current test areas include:

 - Repository tests
- Scheduler tests
- Email service tests
- Redis tests
- User service tests
- User details service tests
- Application context tests

 The test structure is organized under:

```
src/test/java/net/engineeringdigest/journalApp/
├── repository/
├── scheduler/
└── services/
```

### Run Tests

 Using Maven:

```
./mvnw test
```

 On Windows:

```
mvnw.cmd test
```

---

 ## 🔍 Code Quality

 The project integrates **SonarQube/SonarCloud** for static code analysis and code quality monitoring.

 The Maven configuration includes the Sonar Maven plugin and SonarCloud organization configuration.

 You can run a Sonar analysis using:

```
./mvnw sonar:sonar
```

 > Make sure the required SonarCloud/SonarQube credentials and project configuration are available in your environment before running the analysis.

---

 ## ⚙️ Prerequisites

 Before running the project, make sure you have:

 - **Java 8+**
- **Maven** or Maven Wrapper
- **MongoDB**
- **Redis**
- Google OAuth credentials _(if using Google authentication)_
- SMTP credentials _(if using email functionality)_

 The project is configured for Java 8 and Spring Boot 2.7.15.

---

 ## 🚀 Getting Started

 ### 1\. Clone the Repository

```
git clone https://github.com/prince-borad29/journalApp.git
```

 ### 2\. Navigate to the Project

```
cd journalApp
```

 ### 3\. Configure MongoDB

 Make sure MongoDB is running locally or provide your MongoDB connection details through your application configuration.

 Example:

```
spring.data.mongodb.uri=mongodb://localhost:27017/journalDB
```

 ### 4\. Configure Redis

 Make sure Redis is running locally.

 Default Redis configuration:

```
Host: localhost
Port: 6379
```

 ### 5\. Configure Application Properties

 Create/configure your application configuration with the required:

 - MongoDB URI
- Redis configuration
- JWT configuration
- Google OAuth credentials
- SMTP/email configuration

 ### 6\. Build the Project

```
./mvnw clean install
```

 ### 7\. Run the Application

```
./mvnw spring-boot:run
```

 The application will be available at:

```
http://localhost:8080
```

---

 ## 📄 License

 This project currently does not specify a license in the repository. If you intend to make the project open source, consider adding an appropriate license such as MIT.
