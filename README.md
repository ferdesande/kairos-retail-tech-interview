# kairos-retail-tech-interview
An example of a pricing service created with Java 21 and Spring Boot 3 that demonstrates the hexagonal architecture for retail price management

## Technologies

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## Architecture

This project follows hexagonal architecture principles with clear separation of concerns. The global architecture is
defined in the [architecture.puml](src/test/resources/architecture.puml) file.

Several architecture tests have been created to **enforce architectural boundaries** and ensure the hexagonal
architecture principles are not violated.

The tests are based on the library `com.tngtech.archunit:archunit-junit5`.

All architecture tests can be found by searching for the `@ArchTest` annotation in the test sources.

### Code Documentation
Hint comments are added in the code to explain architectural decisions. When a decision might be controversial or 
non-obvious, it's documented for further understanding (even from your future self).

## Project Structure

The project follows a standard Spring Boot application structure:

```
src/main/java/com/kairos/pricing/
├── domain/
│   ├── exception/
│   ├── model/
│   ├── port/
│   └── service/
├── application/
│   └── usecase/
├── infrastructure/
│   ├── config/
│   ├── persistence/
│   │   ├── entity/
│   │   ├── mapper/
│   │   └── repository/
│   └── web/
│       ├── controller/
│       ├── dto/
│       └── exception/
└── PricingApplication.java  # Main application class
```

## Database

Currently, this project uses H2 in-memory database for development and testing.

### Database Setup

The database schema is automatically created from `schema.sql` and populated with sample data from `data.sql` on application startup.

**Configuration:**
- **Schema creation**: Explicit SQL scripts (`schema.sql`)
- **Data initialization**: Sample pricing data (`data.sql`)
- **JPA validation**: Hibernate validates entity mapping against schema (`ddl-auto: validate`)

## Building and Running the Application

### Prerequisites

- Java 21 or higher
- No Maven installation required (uses Maven Wrapper)

### Build

```bash
./mvnw clean install
```

### Run

```bash
./mvnw spring-boot:run
```

The application will start on port 8080.

## H2 Database Console

The H2 database console is enabled and can be accessed at:

```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:mem:pricingdb`
- Username: `sa`
- Password: `password`
