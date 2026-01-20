package com.volunteer.auth.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volunteer.auth.JwtProvider;
import com.volunteer.auth.dto.AuthResponse;
import com.volunteer.auth.repository.UserRepository;
import com.volunteer.entity.User;
import com.volunteer.entity.UserRole;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthService {
    private final OAuthProperties oauthProperties;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, OAuthState> stateStore = new HashMap<>(); // In production, use Redis or database

    public OAuthService(OAuthProperties oauthProperties, UserRepository userRepository, JwtProvider jwtProvider) {
        this.oauthProperties = oauthProperties;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String getAuthorizationUrl(String provider, String redirectUri) {
        OAuthProperties.ProviderConfig config = oauthProperties.getProvider(provider);
        if (config == null) {
            throw new RuntimeException("Unknown OAuth provider: " + provider);
        }

        String clientId = config.getClientId();
        String clientSecret = config.getClientSecret();
        
        // Check if OAuth credentials are configured (not placeholders)
        if (clientId == null || clientId.startsWith("your_") || clientId.isEmpty()) {
            throw new RuntimeException("OAuth " + provider.toUpperCase() + " client_id is not configured. " +
                    "Please set environment variable " + provider.toUpperCase() + "_CLIENT_ID or configure it in application.properties");
        }
        
        if (clientSecret == null || clientSecret.startsWith("your_") || clientSecret.isEmpty()) {
            throw new RuntimeException("OAuth " + provider.toUpperCase() + " client_secret is not configured. " +
                    "Please set environment variable " + provider.toUpperCase() + "_CLIENT_SECRET or configure it in application.properties");
        }

        // Generate state for CSRF protection
        String state = generateState();
        OAuthState oauthState = new OAuthState();
        oauthState.setRedirectUri(redirectUri);
        oauthState.setProvider(provider);
        stateStore.put(state, oauthState);

        String authUrl = config.getAuthorizationUri();
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8);

        if (provider.equals("vk")) {
            // VK OAuth parameters
            return UriComponentsBuilder.fromHttpUrl(authUrl)
                    .queryParam("client_id", clientId)
                    .queryParam("redirect_uri", redirectUri)
                    .queryParam("response_type", "code")
                    .queryParam("scope", "email")
                    .queryParam("state", state)
                    .build().toUriString();
        } else if (provider.equals("yandex")) {
            // Yandex OAuth parameters
            return UriComponentsBuilder.fromHttpUrl(authUrl)
                    .queryParam("client_id", clientId)
                    .queryParam("redirect_uri", redirectUri)
                    .queryParam("response_type", "code")
                    .queryParam("state", state)
                    .build().toUriString();
        }

        throw new RuntimeException("Unsupported provider: " + provider);
    }

    public AuthResponse handleCallback(String provider, String code, String state) {
        OAuthProperties.ProviderConfig config = oauthProperties.getProvider(provider);
        if (config == null) {
            throw new RuntimeException("Unknown OAuth provider: " + provider);
        }

        // Verify state
        OAuthState oauthState = stateStore.get(state);
        if (oauthState == null || !oauthState.getProvider().equals(provider)) {
            throw new RuntimeException("Invalid state parameter");
        }

        // Exchange code for access token
        String accessToken = exchangeCodeForToken(provider, code, config);

        // Get user info from provider
        UserInfo userInfo = getUserInfo(provider, accessToken, config);

        // Find or create user
        User user = findOrCreateUser(provider, userInfo);

        // Generate JWT token
        String jwtToken = jwtProvider.generateToken(user.getId(), user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setUser(user);

        return response;
    }

    public String getAndRemoveRedirectUri(String state) {
        OAuthState oauthState = stateStore.remove(state);
        return oauthState != null ? oauthState.getRedirectUri() : null;
    }

    private String exchangeCodeForToken(String provider, String code, OAuthProperties.ProviderConfig config) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", config.getClientId());
        body.add("client_secret", config.getClientSecret());
        body.add("code", code);
        body.add("redirect_uri", config.getRedirectUri());

        if (provider.equals("vk")) {
            // VK doesn't use grant_type in token request
        } else if (provider.equals("yandex")) {
            // Yandex uses grant_type
            body.add("grant_type", "authorization_code");
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(config.getTokenUri(), request, String.class);

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            return json.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token response", e);
        }
    }

    private UserInfo getUserInfo(String provider, String accessToken, OAuthProperties.ProviderConfig config) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String userInfoUrl = config.getUserInfoUri();
        if (provider.equals("vk")) {
            userInfoUrl += "?access_token=" + accessToken + "&fields=email,screen_name";
        }

        ResponseEntity<String> response = restTemplate.exchange(
                userInfoUrl, HttpMethod.GET, entity, String.class);

        try {
            JsonNode json = objectMapper.readTree(response.getBody());
            UserInfo userInfo = new UserInfo();

            if (provider.equals("vk")) {
                JsonNode responseNode = json.get("response");
                if (responseNode != null && responseNode.isArray() && responseNode.size() > 0) {
                    JsonNode userNode = responseNode.get(0);
                    userInfo.setId(userNode.get("id").asText());
                    userInfo.setEmail(json.has("email") ? json.get("email").asText() : null);
                    userInfo.setName(userNode.has("first_name") && userNode.has("last_name") 
                            ? userNode.get("first_name").asText() + " " + userNode.get("last_name").asText()
                            : userNode.has("screen_name") ? userNode.get("screen_name").asText() : "VK User");
                }
            } else if (provider.equals("yandex")) {
                userInfo.setId(json.get("id").asText());
                userInfo.setEmail(json.has("default_email") ? json.get("default_email").asText() : null);
                userInfo.setName(json.has("display_name") ? json.get("display_name").asText() 
                        : json.has("first_name") ? json.get("first_name").asText() : "Yandex User");
            }

            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse user info response", e);
        }
    }

    private User findOrCreateUser(String provider, UserInfo userInfo) {
        // Try to find user by email first
        if (userInfo.getEmail() != null) {
            return userRepository.findByEmail(userInfo.getEmail())
                    .orElseGet(() -> createOAuthUser(provider, userInfo));
        }

        // If no email, try to find by provider ID in username
        String providerUsername = provider + "_" + userInfo.getId();
        return userRepository.findByUsername(providerUsername)
                .orElseGet(() -> createOAuthUser(provider, userInfo));
    }

    private User createOAuthUser(String provider, UserInfo userInfo) {
        User user = new User();
        user.setRole(UserRole.USER);
        user.setEmail(userInfo.getEmail());
        
        // If no email, use provider-specific username
        if (userInfo.getEmail() == null) {
            user.setUsername(provider + "_" + userInfo.getId());
        } else {
            user.setUsername(userInfo.getName());
        }
        
        // Generate a random password hash (OAuth users don't need password, but field is required)
        user.setPasswordHash("$2a$10$" + Base64.getEncoder().encodeToString(
                new SecureRandom().generateSeed(32))); // Placeholder hash

        return userRepository.save(user);
    }

    private String generateState() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private static class UserInfo {
        private String id;
        private String email;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class OAuthState {
        private String redirectUri;
        private String provider;

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }
    }
}
