package com.volunteer.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility to generate BCrypt hash for admin password
 * Run this as: java -cp "build/classes/java/main;build/libs/*" com.volunteer.util.GenerateAdminPassword
 */
public class GenerateAdminPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String hash = encoder.encode(password);
        
        System.out.println("========================================");
        System.out.println("Admin Password Hash Generator");
        System.out.println("========================================");
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("========================================");
        System.out.println();
        System.out.println("SQL to update admin user:");
        System.out.println("UPDATE users SET password_hash_bcrypt = '" + hash + "' WHERE email = 'admin@volunteer-system.local';");
        System.out.println();
        System.out.println("Or use this hash in the Liquibase changeset:");
        System.out.println(hash);
    }
}
