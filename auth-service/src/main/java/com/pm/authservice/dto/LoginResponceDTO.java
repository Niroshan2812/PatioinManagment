package com.pm.authservice.dto;

public class LoginResponceDTO {
    private final String token;

    public LoginResponceDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
