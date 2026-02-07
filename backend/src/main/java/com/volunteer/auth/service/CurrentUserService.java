package com.volunteer.auth.service;

import com.volunteer.auth.dto.JwtUser;
import com.volunteer.auth.repository.UserRepository;
import com.volunteer.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUser)) {
            throw new RuntimeException("User not authenticated");
        }
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return userRepository.findById(jwtUser.id())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtUser)) {
            throw new RuntimeException("User not authenticated");
        }
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.id();
    }
}
