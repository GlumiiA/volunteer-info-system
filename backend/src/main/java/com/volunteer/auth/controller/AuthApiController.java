package com.volunteer.auth.controller;

import com.volunteer.auth.dto.AuthResponse;
import com.volunteer.auth.dto.LoginRequest;
import com.volunteer.auth.dto.RegisterRequest;
import com.volunteer.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-14T11:02:32.604251800+03:00[Europe/Moscow]", comments = "Generator version: 7.18.0")
@RestController
@RequestMapping("${openapi.volunteerInformationSystem.base-path:/api/v1}")
public class AuthApiController {

    private final NativeWebRequest request;
    private final AuthService authService;

    @Autowired
    public AuthApiController(NativeWebRequest request, AuthService authService) {
        this.request = request;
        this.authService = authService;
    }

    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getFullName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }
}
