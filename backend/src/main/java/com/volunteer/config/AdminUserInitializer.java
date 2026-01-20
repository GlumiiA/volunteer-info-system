package com.volunteer.config;

import com.volunteer.auth.repository.UserRepository;
import com.volunteer.entity.User;
import com.volunteer.entity.UserRole;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initializes default admin user on application startup
 * Creates admin@volunteer-system.local with password admin123 if it doesn't exist
 */
@Component
public class AdminUserInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);
    
    private static final String ADMIN_EMAIL = "admin@volunteer-system.local";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_USERNAME = "Администратор";
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostConstruct
    public void initializeAdminUser() {
        try {
            // Check if admin user already exists
            if (userRepository.findByEmail(ADMIN_EMAIL).isPresent()) {
                logger.info("Admin user already exists, skipping initialization");
                return;
            }
            
            // Create admin user
            User admin = new User();
            admin.setEmail(ADMIN_EMAIL);
            admin.setUsername(ADMIN_USERNAME);
            admin.setRole(UserRole.ADMIN);
            admin.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setVolunteerHours(0f);
            admin.setRating(5.0f);
            
            userRepository.save(admin);
            
            logger.info("Default admin user created successfully");
            logger.info("Email: {}", ADMIN_EMAIL);
            logger.info("Password: {}", ADMIN_PASSWORD);
        } catch (Exception e) {
            logger.error("Failed to initialize admin user", e);
        }
    }
}
