package com.pm.patient_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GloableExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GloableExceptionHandler.class);
    // Centralized Exception handling logic

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // Custom Exception
    @ExceptionHandler(EmailAlredyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlredyExistsException(EmailAlredyExistsException ex) {
        log.warn("Email address alredy exists exception {}",ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Email Alredy Exists");
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(PationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePationNotFoundException(PationNotFoundException ex) {
        log.warn("Patient not found exception {}",ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Patient Not Found");
        return ResponseEntity.badRequest().body(errors);
    }

}
