# British Spoken Time - REST API

Converts times (HH:mm) into British spoken form.

## Features
- REST endpoints supporting `?time=HH:mm` and `/hour/minute`.
- Handles special cases: `midnight`, `noon`, `o'clock`, `quarter past/to`, `half past`.
- Strategy-based formatter and factory for extensibility.
- Clear error handling (400 on invalid input).

## Requirements
- Java 17+
- Maven

## Build & Run
```bash
# build
mvn clean package

# run
mvn spring-boot:run

# or run jar
java -jar target/british-spoken-time-1.0.0.jar
