package com.pm.patient_service.exception;

public class EmailAlredyExistsException extends RuntimeException {
    public EmailAlredyExistsException(String message) {
        super(message);
    }
}
