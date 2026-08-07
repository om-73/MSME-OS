package com.msme.erp.controller;

import com.msme.erp.dto.LoginRequest;
import com.msme.erp.dto.LoginResponse;
import com.msme.erp.dto.RegisterTenantRequest;
import com.msme.erp.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register-tenant")
    public ResponseEntity<LoginResponse> registerTenant(@RequestBody RegisterTenantRequest request) {
        return ResponseEntity.ok(authService.registerTenant(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
