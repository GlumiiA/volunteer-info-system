-- ============================================
-- Script to fix admin password
-- Run this if you cannot login with admin credentials
-- ============================================

-- Option 1: If admin user exists but password is wrong
-- First, you need to generate a BCrypt hash for "admin123"
-- Use the Java utility: com.volunteer.util.GenerateAdminPassword
-- Or use an online BCrypt generator
-- Then update:
-- UPDATE users SET password_hash_bcrypt = '<YOUR_GENERATED_HASH>' WHERE email = 'admin@volunteer-system.local';

-- Option 2: Delete and recreate admin user
-- DELETE FROM users WHERE email = 'admin@volunteer-system.local';
-- Then restart the backend to let Liquibase create it again

-- Option 3: Create admin user manually (if it doesn't exist)
-- You'll need to generate a BCrypt hash first
INSERT INTO users (username, email, password_hash_bcrypt, role, volunteer_hours, rating)
SELECT 
    'Администратор',
    'admin@volunteer-system.local',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- This is a test hash, may not work for "admin123"
    'ADMIN',
    0,
    5.0
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@volunteer-system.local'
);

-- Check current admin user
SELECT id, username, email, role, 
       CASE 
           WHEN password_hash_bcrypt LIKE '$2a$10$%' THEN 'BCrypt hash present'
           ELSE 'Invalid hash format'
       END as hash_status
FROM users 
WHERE email = 'admin@volunteer-system.local';
