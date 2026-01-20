-- Test Data Script for Volunteer Information System
-- Database: postgres
-- This script creates test data for all tables

-- Clear existing test data (optional - comment out if you want to keep existing data)
-- TRUNCATE TABLE participations CASCADE;
-- TRUNCATE TABLE organization_role_requests CASCADE;
-- TRUNCATE TABLE volunteer_book_entries CASCADE;
-- TRUNCATE TABLE reviews CASCADE;
-- TRUNCATE TABLE mass_events CASCADE;
-- TRUNCATE TABLE individual_events CASCADE;
-- TRUNCATE TABLE users CASCADE;
-- TRUNCATE TABLE organisations CASCADE;

-- Organizations
INSERT INTO organisations (name, description, address) VALUES
('Благотворительный фонд "Доброе сердце"', 
 'Помощь нуждающимся семьям и детям', 
 'г. Санкт-Петербург, ул. Ленина, д. 15')
ON CONFLICT DO NOTHING;

INSERT INTO organisations (name, description, address) VALUES
('Экологическое движение "Чистый город"', 
 'Защита окружающей среды и озеленение города', 
 'г. Санкт-Петербург, пр. Просвещения, д. 45')
ON CONFLICT DO NOTHING;

INSERT INTO organisations (name, description, address) VALUES
('Фонд помощи животным "Верный друг"', 
 'Помощь бездомным животным и организация приютов', 
 'г. Санкт-Петербург, ул. Маршала Захарова, д. 8')
ON CONFLICT DO NOTHING;

-- Users
-- Password for all test users: "password123" (bcrypt hash)
-- BCrypt hash for "password123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT INTO users (organisation_id, role, username, email, description, birthday, location, volunteer_hours, rating, password_hash_bcrypt) VALUES
-- Admin user
(NULL, 'ADMIN', 'admin@test.com', 'admin@test.com', 'System Administrator', '1985-03-15', 'Санкт-Петербург', 0, 5.0, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (username) DO NOTHING;

-- Organization representatives
(1, 'ORG_REPRESENTATIVE', 'petrova@test.com', 'petrova@test.com', 'Координатор волонтерских программ', '1990-07-22', 'Санкт-Петербург', 150, 4.9, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (organisation_id, role, username, email, description, birthday, location, volunteer_hours, rating, password_hash_bcrypt) VALUES
(2, 'ORG_REPRESENTATIVE', 'sidorov@test.com', 'sidorov@test.com', 'Руководитель экологических проектов', '1988-11-30', 'Санкт-Петербург', 200, 4.8, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (username) DO NOTHING;

-- Regular users
INSERT INTO users (organisation_id, role, username, email, description, birthday, location, volunteer_hours, rating, password_hash_bcrypt) VALUES
(NULL, 'USER', 'user1@test.com', 'user1@test.com', 'Люблю помогать людям', '2000-01-15', 'Санкт-Петербург', 45, 4.5, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (organisation_id, role, username, email, description, birthday, location, volunteer_hours, rating, password_hash_bcrypt) VALUES
(NULL, 'USER', 'user2@test.com', 'user2@test.com', 'Интересуюсь экологией', '1998-06-20', 'Санкт-Петербург', 78, 4.6, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (username) DO NOTHING;

-- Mass Events (using current timestamp)
INSERT INTO mass_events (organisation_id, title, description, date_start, date_end, work_hours, address, volunteers_required, age_restriction) VALUES
(1, 'Помощь детскому дому "Солнышко"', 
 'Организация праздника для детей, ремонт и благоустройство территории', 
 CURRENT_TIMESTAMP + INTERVAL '5 days', 
 CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '8 hours', 
 8.0, 
 'г. Санкт-Петербург, ул. Детская, д. 10', 
 15, 
 18)
ON CONFLICT DO NOTHING;

INSERT INTO mass_events (organisation_id, title, description, date_start, date_end, work_hours, address, volunteers_required, age_restriction) VALUES
(2, 'Субботник в парке "Зеленый остров"', 
 'Уборка территории парка, посадка деревьев и кустарников', 
 CURRENT_TIMESTAMP + INTERVAL '10 days', 
 CURRENT_TIMESTAMP + INTERVAL '10 days' + INTERVAL '6 hours', 
 6.0, 
 'г. Санкт-Петербург, парк "Зеленый остров"', 
 30, 
 16)
ON CONFLICT DO NOTHING;

INSERT INTO mass_events (organisation_id, title, description, date_start, date_end, work_hours, address, volunteers_required, age_restriction) VALUES
(3, 'День помощи приюту для животных', 
 'Уход за животными, уборка помещений, социализация животных', 
 CURRENT_TIMESTAMP + INTERVAL '15 days', 
 CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '5 hours', 
 5.0, 
 'г. Санкт-Петербург, ул. Приютская, д. 7', 
 20, 
 16)
ON CONFLICT DO NOTHING;

-- Individual Events (organisation_id here stores user ID for individual events)
-- Get user IDs first (assuming user1@test.com has id 4 and user2@test.com has id 5)
-- We'll insert these with a subquery or use a fixed ID if we know it
INSERT INTO individual_events (organisation_id, title, description, date_start, date_end, volunteers_required, age_restriction) 
SELECT 
    u.id, 
    'Помощь в переезде пожилой соседке', 
    'Требуется помощь в упаковке вещей и переносе мебели', 
    CURRENT_TIMESTAMP + INTERVAL '3 days', 
    CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '4 hours', 
    2, 
    18
FROM users u 
WHERE u.username = 'user1@test.com' 
LIMIT 1
ON CONFLICT DO NOTHING;

INSERT INTO individual_events (organisation_id, title, description, date_start, date_end, volunteers_required, age_restriction) 
SELECT 
    u.id, 
    'Присмотр за домашними животными', 
    'Необходим присмотр за двумя кошками во время отъезда', 
    CURRENT_TIMESTAMP + INTERVAL '7 days', 
    CURRENT_TIMESTAMP + INTERVAL '14 days', 
    1, 
    16
FROM users u 
WHERE u.username = 'user2@test.com' 
LIMIT 1
ON CONFLICT DO NOTHING;

-- Participations
-- First, get the mass event IDs and user IDs, then insert participations
INSERT INTO participations (user_id, event_id, event_type, status, application_date)
SELECT 
    u.id,
    me.id,
    'MASS',
    'ACCEPTED',
    CURRENT_TIMESTAMP - INTERVAL '2 days'
FROM users u, mass_events me
WHERE u.username = 'user1@test.com' 
  AND me.title = 'Помощь детскому дому "Солнышко"'
LIMIT 1
ON CONFLICT DO NOTHING;

INSERT INTO participations (user_id, event_id, event_type, status, application_date)
SELECT 
    u.id,
    me.id,
    'MASS',
    'PENDING',
    CURRENT_TIMESTAMP - INTERVAL '1 day'
FROM users u, mass_events me
WHERE u.username = 'user2@test.com' 
  AND me.title = 'Помощь детскому дому "Солнышко"'
LIMIT 1
ON CONFLICT DO NOTHING;

-- Display summary
SELECT 'Organizations' AS table_name, COUNT(*) AS count FROM organisations
UNION ALL
SELECT 'Users', COUNT(*) FROM users
UNION ALL
SELECT 'Mass Events', COUNT(*) FROM mass_events
UNION ALL
SELECT 'Individual Events', COUNT(*) FROM individual_events
UNION ALL
SELECT 'Participations', COUNT(*) FROM participations;
