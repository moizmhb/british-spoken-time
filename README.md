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

---

## Design Patterns

### **1. Strategy Pattern (Implemented)**

**Intent:**  
Encapsulates time-formatting logic per locale (e.g., British, German, Czech). Allows adding new locale rules without modifying existing code.

**Implementation:**
```java
public interface TimeFormatterStrategy {
    String formatTime(int hour, int minute);
}
```
**British Implementation:**
```java
public class BritishTimeFormatter implements TimeFormatterStrategy {
    @Override
    public String formatTime(int hour, int minute) {
        // Implements British-specific rules (e.g., “quarter past”, “half past”)
    }
}
```

**Advantages:**
- Complies with **Open/Closed Principle (OCP)**
- Supports easy extension for new locales
- Keeps code modular and testable

---

### **2. Factory Pattern (Extensible)**

**Intent:**  
Centralizes creation of appropriate formatter strategy based on locale.

**Implementation:**
```java
public class TimeFormatterFactory {
    public static TimeFormatterStrategy getFormatter(String locale) {
        switch (locale.toLowerCase()) {
            case "de": return new GermanTimeFormatter();
            case "cz": return new CzechTimeFormatter();
            default: return new BritishTimeFormatter();
        }
    }
}
```

**Advantages:**
- Isolates object creation
- Allows dynamic strategy selection
- Reduces coupling

---

# SOLID Principles in British Spoken Time API

This project adheres to **SOLID principles** to ensure clean, extensible, and maintainable code architecture.  
Each principle is applied through clear separation of concerns, dependency injection, and interface-driven design.

---

## 1. **Single Responsibility Principle (SRP)**
**Definition:** A class should have one and only one reason to change.

**Implementation:**
- **`TimeController`** → Handles REST endpoints and HTTP interactions only.
- **`TimeService`** → Orchestrates business logic and delegates to the strategy.
- **`BritishTimeFormatter`** → Converts digital time into spoken form (British rules).
- **`InvalidTimeFormatException`** → Encapsulates input validation errors.


---

## 2. **Open/Closed Principle (OCP)**
**Definition:** Software entities should be open for extension, but closed for modification.

**Implementation:**
- The **Strategy Pattern** allows adding new locale-based formatters (e.g., `GermanTimeFormatter`, `CzechTimeFormatter`)  
  without changing existing code.
- The system is extensible through new implementations of `TimeFormatterStrategy`.


---

## 3. **Liskov Substitution Principle (LSP)**
**Definition:** Objects of a superclass should be replaceable with objects of its subclasses without affecting correctness.

**Implementation:**
- All formatters (British, etc ) implement the same interface:
  ```java
  public interface TimeFormatterStrategy {
      String formatTime(int hour, int minute);
  }


## 4. **Interface Segregation Principle (ISP)**
**Definition:** Clients should not be forced to depend on interfaces they do not use.

**Implementation:**
- The `TimeFormatterStrategy` interface exposes only one focused method:
  ```java
  String formatTime(int hour, int minute);
  ```
- Keeps the interface small, specific, and easy to implement.



---

## 5. **Dependency Inversion Principle (DIP)**
**Definition:** High-level modules should depend on abstractions, not on concrete implementations.

**Implementation:**
- `TimeService` depends on the abstraction `TimeFormatterStrategy`, not a concrete `BritishTimeFormatter`.
- Spring Boot injects the appropriate strategy bean at runtime.

**Example:**
```java
@Service
public class TimeService {
    private final TimeFormatterStrategy formatter;

    public TimeService(TimeFormatterStrategy formatter) {
        this.formatter = formatter;
    }
}
```

## UML For Strategy & Factory Design Pattern
               +----------------------------+
               |      TimeFormatterStrategy |
               +----------------------------+
               | + formatTime(hour, minute) |
               +-------------+--------------+
                             ▲
                    +--------------------+-
                     |                    |                      
            +------------------+  +------------------+  
            | BritishTimeFormatter |  | GermanTimeFormatter  | 
            +------------------+  +------------------+  
            | + formatTime(...) |     | + formatTime(...) | 
            +------------------+     +------------------+  

                        ▲
                        |
            +--------------------------+
            |   TimeFormatterFactory   |
            +--------------------------+
            | + getFormatter(locale)   |
            +--------------------------+
                        |
                        ▼
             +------------------------+
             |     TimeService        |
             +------------------------+
             | + toSpokenTime(time)   |
             | + toSpokenTime(h,m)    |
             +------------------------+
                        |
                        ▼
             +------------------------+
             |     TimeController     |
             +------------------------+
             | + /spoken-time APIs    |
             +------------------------+

