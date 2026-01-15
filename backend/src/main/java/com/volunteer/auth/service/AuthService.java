package com.volunteer.auth.service;

import com.volunteer.auth.JwtProvider;
import com.volunteer.auth.repository.UserRepository;
import com.volunteer.auth.dto.AuthResponse;
import com.volunteer.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.volunteer.entity.UserRole;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AuthResponse register(String email, String password, String fullName) {
        // Проверяем, существует ли пользователь
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        // Создаем нового пользователя
        User user = new User();
        user.setEmail(email);
        user.setUsername(fullName);
        user.setRole(UserRole.USER);
        user.setPasswordHash(passwordEncoder.encode(password));

        // Сохраняем пользователя
        User savedUser = userRepository.save(user);

        // Генерируем токен
        String token = jwtProvider.generateToken(Long.valueOf(savedUser.getId()), savedUser.getEmail());

        // Формируем ответ
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(savedUser);

        return response;
    }

    public AuthResponse login(String email, String password) {
        // Ищем пользователя по email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Проверяем пароль
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Генерируем токен
        String token = jwtProvider.generateToken(Long.valueOf(user.getId()), user.getEmail());

        // Формируем ответ
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(user);

        return response;
    }
}
