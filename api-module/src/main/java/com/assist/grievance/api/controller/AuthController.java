package com.assist.grievance.api.controller;

import com.assist.grievance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/token")
    @Operation(summary = "Legacy token endpoint", description = "Keycloak integration has been disabled in this service.")
    public ResponseEntity<Map<String, Object>> exchangeTokenbyCode(@RequestParam String authorization_code, @RequestParam String redirectUri) {
        return authService.codeTokenExchange(authorization_code, redirectUri);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Legacy refresh endpoint", description = "Keycloak integration has been disabled in this service.")
    public ResponseEntity<Map<String, Object>> getTokenFromExchangeToken(@RequestParam String refreshToken) {
        return authService.refreshTokenExchange(refreshToken);
    }

}
