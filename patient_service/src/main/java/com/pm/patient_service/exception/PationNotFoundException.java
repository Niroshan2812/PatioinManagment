package com.pm.patient_service.exception;

public class PationNotFoundException extends RuntimeException {
    public PationNotFoundException(String message) {
        super(message);
    }
}
