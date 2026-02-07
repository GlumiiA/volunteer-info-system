package com.volunteer.auth.controller;

import com.volunteer.auth.dto.AuthResponse;
import com.volunteer.auth.dto.LoginRequest;
import com.volunteer.auth.dto.RegisterRequest;
import com.volunteer.auth.oauth.OAuthService;
import com.volunteer.auth.service.AuthService;
import com.volunteer.common.dto.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final OAuthService oauthService;

    @Autowired
    public AuthApiController(NativeWebRequest request, AuthService authService, OAuthService oauthService) {
        this.request = request;
        this.authService = authService;
        this.oauthService = oauthService;
    }

    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse response = authService.register(
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getFullName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Error error = new Error();
            error.setCode("REGISTRATION_ERROR");
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Error error = new Error();
            error.setCode("LOGIN_ERROR");
            error.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    @GetMapping("/auth/oauth2/{provider}/authorize")
    public ResponseEntity<?> oauth2Authorize(
            @PathVariable String provider,
            @RequestParam String redirect_uri) {
        if (!provider.equals("vk") && !provider.equals("yandex")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String authorizationUrl = oauthService.getAuthorizationUrl(provider, redirect_uri);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", authorizationUrl)
                    .build();
        } catch (RuntimeException e) {
            // Return error message if OAuth is not configured
            if (e.getMessage() != null && e.getMessage().contains("not configured")) {
                Error error = new Error();
                error.setCode("OAUTH_NOT_CONFIGURED");
                error.setMessage(e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(error);
            }
            Error error = new Error();
            error.setCode("OAUTH_ERROR");
            error.setMessage("OAuth authorization failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        } catch (Exception e) {
            Error error = new Error();
            error.setCode("OAUTH_ERROR");
            error.setMessage("OAuth authorization failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(error);
        }
    }

    @GetMapping("/auth/oauth2/{provider}/callback")
    public ResponseEntity<?> oauth2Callback(
            @PathVariable String provider,
            @RequestParam String code,
            @RequestParam(required = false) String state) {
        if (!provider.equals("vk") && !provider.equals("yandex")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            AuthResponse response = oauthService.handleCallback(provider, code, state);
            String frontendRedirectUri = oauthService.getAndRemoveRedirectUri(state);
            
            if (frontendRedirectUri != null && !frontendRedirectUri.isEmpty()) {
                // Redirect to frontend with token in URL fragment or as query parameter
                String redirectUrl = frontendRedirectUri + "?token=" + response.getToken();
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header("Location", redirectUrl)
                        .build();
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        // JWT tokens are stateless, so logout is handled client-side by removing the token
        // In a production system, you might want to maintain a blacklist of tokens
        return ResponseEntity.ok().build();
    }
}
