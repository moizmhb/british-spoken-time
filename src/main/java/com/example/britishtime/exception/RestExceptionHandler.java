package com.example.britishtime.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(InvalidTimeException.class)
    public ResponseEntity<?> handleInvalid(InvalidTimeException ex) {
        return ResponseEntity.badRequest().body(new ErrorPayload("invalid_time", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAny(Exception ex) {
        return ResponseEntity.internalServerError().body(new ErrorPayload("server_error", ex.getMessage()));
    }

    record ErrorPayload(String code, String message) {}
}
