package com.assist.grievance.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    public ResponseEntity<Map<String, Object>> codeTokenExchange(String authorization_code, String redirectUri) {
        return keycloakDisabledResponse();
    }

    public ResponseEntity<Map<String, Object>> refreshTokenExchange(String refreshToken) {
        return keycloakDisabledResponse();
    }

    private ResponseEntity<Map<String, Object>> keycloakDisabledResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Keycloak auth is disabled for this service.");
        return ResponseEntity.status(HttpStatus.GONE).body(response);
    }
}
