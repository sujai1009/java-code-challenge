# Devices API

A REST API for persisting and managing device resources, built with Spring Boot 3.2.0 and Java 21.

## API Documentation

- [OpenAPI Specification (JSON)](http://localhost:8080/v3/api-docs)
- [Swagger UI](http://localhost:8080/swagger-ui.html)

## Features

- Create a new device
- Fully and/or partially update an existing device
- Fetch a single device by ID
- Fetch all devices (with pagination)
- Fetch devices by brand
- Fetch devices by state
- Fetch all distinct brands
- Delete a single device

## Domain Model

### Device
- `id` - Unique identifier (auto-generated)
- `name` - Device name
- `brand` - Device brand
- `state` - Device state (AVAILABLE, IN_USE, INACTIVE)
- `creationTime` - Timestamp of creation (cannot be updated)
- `updatedAt` - Timestamp of last update (automatically set)

## Domain Validations

- Creation time cannot be updated
- Name and brand properties cannot be updated if the device is in use
- In-use devices cannot be deleted

## Technology Stack

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database (file-based, persistent)
- Lombok
- Maven
- Docker (for containerization)
- Testcontainers (for integration tests)

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/devices` | Create a new device |
| GET | `/api/devices/{id}` | Fetch a single device by ID |
| GET | `/api/devices` | Fetch all devices (with pagination) |
| GET | `/api/devices/brand/{brand}` | Fetch devices by brand |
| GET | `/api/devices/state/{state}` | Fetch devices by state |
| GET | `/api/devices/brands` | Fetch all distinct brands |
| PATCH | `/api/devices/{id}` | Update a device (fully or partially) |
| DELETE | `/api/devices/{id}` | Delete a device |

### Pagination

The `GET /api/devices` endpoint supports pagination via standard Spring Data `Pageable` parameters:

- `page` (default: 0) — zero-based page index
- `size` (default: 20) — page size
- `sort` (optional) — sorting criteria in the format `property,asc` or `property,desc`

Example: `GET /api/devices?page=0&size=10&sort=name,asc`

The response includes pagination metadata:

```json
{
  "success": true,
  "data": {
    "content": [...],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "lastPage": false
  }
}
```

## Running the Application

### Prerequisites

- Java 21+
- Maven 3.9+

### Local Development

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`.

### Running with Docker

```bash
To clean start  `docker compose up --build`
To start        `docker compose up -d`
To stop         `docker compose down -v``
```

## Running Tests

```bash
./mvnw test
```

### Test Coverage

This project uses [JaCoCo](https://www.jacoco.org/) for code coverage reporting.

```bash
# Run tests and generate coverage report
./mvnw clean test jacoco:report
```

The HTML coverage report will be generated at:

```
target/site/jacoco/index.html
```

Open it in your browser to view detailed coverage metrics per class, method, and line.

- Unit tests for the controller layer (9 tests)
- Service layer tests (12 tests)
- Concurrency tests with optimistic locking (5 tests)
- Validation tests (13 tests)
- Integration tests with Testcontainers (requires Docker)

## Project Structure

```
src/main/java/com/sujai/test/
├── ApiApplication.java          # Main application class
├── controller/
│   ├── DeviceController.java    # REST controller
│   └── GlobalExceptionHandler.java
├── model/
│   ├── Device.java              # JPA Entity
│   └── DeviceState.java         # Enum for device states
├── repository/
│   └── DeviceRepository.java    # Spring Data JPA repository
└── service/
    └── DeviceService.java       # Business logic layer
```

## Additional Improvements

Completed:
- Added concurrent update handling with Optimistic locking
- Add OpenAPI/Swagger documentation
- Add sorting and filtering capabilities
- Add input validation with custom validators
- Add integration tests for the service layer
- Add health check endpoints

Incomplete:
- Brand can be moved to a separate table rather than a free flow code



## License

This project is licensed under the MIT License.
