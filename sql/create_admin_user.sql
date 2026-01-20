-- ============================================
-- Script to create admin user
-- Run this after the database is set up
-- ============================================

-- Insert admin user (only if doesn't exist)
-- Password: admin123
-- BCrypt hash: $2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
-- Note: This is a standard test hash. For production, generate a new hash using BCryptPasswordEncoder
INSERT INTO users (username, email, password_hash_bcrypt, role, volunteer_hours, rating)
SELECT 
    'Администратор',
    'admin@volunteer-system.local',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- Password: admin123
    'ADMIN',
    0,
    5.0
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@volunteer-system.local'
);

-- Verify admin user was created
SELECT id, username, email, role FROM users WHERE email = 'admin@volunteer-system.local';
