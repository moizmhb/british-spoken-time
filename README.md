# British Spoken Time - REST API

Convert digital time (`HH:mm`) into **British English spoken form** — for example:  
`07:30 → "half past seven"`

---

## Problem Statement

In **British English**, people express time using distinct patterns.  
For instance:
- “Half ten” means **10:30** (“half past ten”)
- “Quarter to eight” means **7:45**
- “Five past six” means **6:05**

This project implements a **Java REST API** that converts 24-hour formatted times into **British spoken expressions**, handling special cases like “midnight” and “noon.”

---

## Features

- REST API built with Spring Boot 3 & Java 17
- Follows OOP, SRP, and OCP principles
- Clean exception handling (`InvalidTimeFormatException`)
- Full unit & integration test coverage (JUnit 5, MockMvc)
- Includes Swagger UI for easy testing
- Supports CSV upload for batch time conversion

---

## Requirements

- Accepts time in 24-hour format (`HH:mm`)
- Returns **spoken British English time**
- Handles special cases:
    - `00:00` → “midnight”
    - `12:00` → “noon”
- Validates invalid inputs (e.g., `25:99`)
- Follows **OOP principles** and **clean code practices**
- Includes **unit and integration tests**
- Extensible to other locales (e.g., German, Czech)

---

## Example Conversions

| Digital | Spoken Form |
|----------|--------------|
| 1:00 | one o'clock |
| 2:05 | five past two |
| 3:10 | ten past three |
| 4:15 | quarter past four |
| 5:20 | twenty past five |
| 6:25 | twenty five past six |
| 6:32 | six thirty two |
| 7:30 | half past seven |
| 7:35 | twenty five to eight |
| 8:40 | twenty to nine |
| 9:45 | quarter to ten |
| 10:50 | ten to eleven |
| 11:55 | five to twelve |
| 00:00 | midnight |
| 12:00 | noon |

---

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Maven**
- **JUnit 5 / MockMvc**
- **Checkstyle**
- **Jacoco (for test coverage)**
- *(Optional)* GitHub Actions CI/CD

---

## How to Run

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/british-spoken-time.git
cd british-spoken-time
```

### 2. Build and Run
```bash
mvn clean package
mvn spring-boot:run
```

---

## Swagger Documentation

### Swagger Dependency (Maven)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

### Swagger Configuration (application.yml)
```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method
    tagsSorter: alpha
    displayRequestDuration: true
```

### Swagger Access
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## REST API Endpoints

### 1. Query Parameter Example
**GET** `/api/v1/spoken-time?time=07:30`

**Response:**
```json
{
  "original": "07:30",
  "spoken": "half past seven"
}
```

---

### 2. Path Variables Example
**GET** `/api/v1/spoken-time/9/45`

**Response:**
```json
{
  "original": "09:45",
  "spoken": "quarter to ten"
}
```

---

### 3. Invalid Input Example
**GET** `/api/v1/spoken-time?time=25:99`

**Response:**
```json
{
  "code": "invalid_time",
  "message": "hour must be between 0 and 23"
}
```

---

## CSV Upload Endpoint

**POST** `/api/v1/spoken-time/upload`  
Upload a CSV file (e.g., `times.csv`) containing comma-separated times:

Example CSV:
```csv
07:30,09:45,12:00
00:00,01:15,05:20
```

**Response:**
```json
[
  { "original": "07:30", "spoken": "half past seven" },
  { "original": "09:45", "spoken": "quarter to ten" },
  { "original": "12:00", "spoken": "noon" },
  { "original": "00:00", "spoken": "midnight" },
  { "original": "01:15", "spoken": "quarter past one" },
  { "original": "05:20", "spoken": "twenty past five" }
]
```

---

## Tests

### Run All Tests
```bash
mvn test
```

### Test Coverage
- `TimeServiceTest` — Unit tests for service logic
- `TimeControllerCsvTest` — Integration test for CSV upload endpoint

---

## Project Structure

```
src/
├── main/
│   ├── java/com/example/britishtime/
│   │   ├── controller/TimeController.java
│   │   ├── model/TimeResponse.java
│   │   ├── service/TimeService.java
│   │   ├── exception/InvalidTimeFormatException.java
│   └── resources/application.yml
├── test/
│   ├── java/com/example/britishtime/
│   │   ├── controller/TimeControllerCsvTest.java
│   │   ├── service/TimeServiceTest.java
│   └── resources/times.csv
```

---

## Design Notes

- Extensible to add other time formats
- Exception handling via `@ControllerAdvice`
- Unit and integration tests for reliability

---

