# kairos-retail-tech-interview
An example of a pricing service created with Java 21 and Spring Boot 3 that demonstrates the hexagonal architecture for retail price management

## Technologies

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## Project Structure

The project follows a standard Spring Boot application structure:

```
src/main/java/com/kairos/pricing/
└── PricingApplication.java  # Main application class
```

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
