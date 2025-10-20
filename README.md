#  British Spoken Time - REST API

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

##  Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Maven**
- **JUnit 5 / MockMvc**
- **Checkstyle**
- **Jacoco (for coverage)**
- *(Optional)* GitHub Actions CI/CD

---

## How to Run

### Clone Repository
```bash
git clone https://github.com/yourusername/british-spoken-time.git
cd british-spoken-time
mvn clean package
mvn spring-boot:run
```


## REST API Endpoints
### 1️. Query Parameter Example
GET /api/v1/spoken-time?time=07:30
Response:

{
"original": "07:30",
"spoken": "half past seven"
}

### 2. Path Variables Example
GET /api/v1/spoken-time/9/45


Response:

{
"original": "09:45",
"spoken": "quarter to ten"
}

### 3. Invalid Input Example
GET /api/v1/spoken-time?time=25:99


Response:

{
"code": "invalid_time",
"message": "hour must be between 0 and 23"
}
