-- Test Data Script for Volunteer Information System
-- Database: postgres
-- Execute with: psql -h localhost -U postgres -d postgres -f insert_test_data.sql

-- Organizations
INSERT INTO organisations (name, description, address) VALUES
('Благотворительный фонд "Доброе сердце"', 
 'Помощь нуждающимся семьям и детям', 
 'г. Санкт-Петербург, ул. Ленина, д. 15'),
('Экологическое движение "Чистый город"', 
 'Защита окружающей среды и озеленение города', 
 'г. Санкт-Петербург, пр. Просвещения, д. 45'),
('Фонд помощи животным "Верный друг"', 
 'Помощь бездомным животным и организация приютов', 
 'г. Санкт-Петербург, ул. Маршала Захарова, д. 8')
ON CONFLICT DO NOTHING;

-- Users with bcrypt hashed password for "password123"
-- Note: Use registration endpoint or generate hash programmatically for production
INSERT INTO users (organisation_id, role, username, email, description, birthday, location, volunteer_hours, rating, password_hash_bcrypt) VALUES
(NULL, 'ADMIN', 'admin@test.com', 'admin@test.com', 'System Administrator', '1985-03-15', 'Санкт-Петербург', 0, 5.0, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(1, 'ORG_REPRESENTATIVE', 'org1@test.com', 'org1@test.com', 'Координатор волонтерских программ', '1990-07-22', 'Санкт-Петербург', 150, 4.9, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(2, 'ORG_REPRESENTATIVE', 'org2@test.com', 'org2@test.com', 'Руководитель экологических проектов', '1988-11-30', 'Санкт-Петербург', 200, 4.8, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(NULL, 'USER', 'user1@test.com', 'user1@test.com', 'Люблю помогать людям', '2000-01-15', 'Санкт-Петербург', 45, 4.5, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
(NULL, 'USER', 'user2@test.com', 'user2@test.com', 'Интересуюсь экологией', '1998-06-20', 'Санкт-Петербург', 78, 4.6, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON CONFLICT (username) DO UPDATE SET
    organisation_id = EXCLUDED.organisation_id,
    role = EXCLUDED.role,
    email = EXCLUDED.email;

-- Mass Events
INSERT INTO mass_events (organisation_id, title, description, date_start, date_end, work_hours, address, volunteers_required, age_restriction) VALUES
(1, 'Помощь детскому дому "Солнышко"', 
 'Организация праздника для детей, ремонт и благоустройство территории', 
 CURRENT_TIMESTAMP + INTERVAL '5 days', 
 CURRENT_TIMESTAMP + INTERVAL '5 days' + INTERVAL '8 hours', 
 8.0, 
 'г. Санкт-Петербург, ул. Детская, д. 10', 
 15, 
 18),
(2, 'Субботник в парке "Зеленый остров"', 
 'Уборка территории парка, посадка деревьев и кустарников', 
 CURRENT_TIMESTAMP + INTERVAL '10 days', 
 CURRENT_TIMESTAMP + INTERVAL '10 days' + INTERVAL '6 hours', 
 6.0, 
 'г. Санкт-Петербург, парк "Зеленый остров"', 
 30, 
 16),
(3, 'День помощи приюту для животных', 
 'Уход за животными, уборка помещений, социализация животных', 
 CURRENT_TIMESTAMP + INTERVAL '15 days', 
 CURRENT_TIMESTAMP + INTERVAL '15 days' + INTERVAL '5 hours', 
 5.0, 
 'г. Санкт-Петербург, ул. Приютская, д. 7', 
 20, 
 16)
ON CONFLICT DO NOTHING;

-- Individual Events (organisation_id stores user ID for individual events)
-- Using subquery to get user IDs
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

-- Participations (using subqueries to get IDs)
INSERT INTO participations (user_id, event_id, event_type, status, application_date)
SELECT 
    u.id,
    me.id,
    'MASS',
    'ACCEPTED',
    CURRENT_TIMESTAMP - INTERVAL '2 days'
FROM users u
CROSS JOIN mass_events me
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
FROM users u
CROSS JOIN mass_events me
WHERE u.username = 'user2@test.com' 
  AND me.title = 'Помощь детскому дому "Солнышко"'
LIMIT 1
ON CONFLICT DO NOTHING;

-- Display summary
SELECT 'Test Data Summary' AS info;
SELECT 'Organizations' AS table_name, COUNT(*) AS count FROM organisations
UNION ALL
SELECT 'Users', COUNT(*) FROM users
UNION ALL
SELECT 'Mass Events', COUNT(*) FROM mass_events
UNION ALL
SELECT 'Individual Events', COUNT(*) FROM individual_events
UNION ALL
SELECT 'Participations', COUNT(*) FROM participations;
