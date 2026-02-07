-- ============================================
-- Скрипт удаления базы данных
-- Система управления волонтерскими мероприятиями
-- ============================================

-- Отключение всех активных подключений к базе данных
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'volunteer_system'
  AND pid <> pg_backend_pid();

-- Удаление базы данных
DROP DATABASE IF EXISTS volunteer_system;

-- Альтернативный вариант: удаление только таблиц и типов (если нужно сохранить саму БД)
/*
\c volunteer_system;

-- Удаление таблиц (в порядке зависимостей)
DROP TABLE IF EXISTS leaderboard CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS volunteer_book_entry CASCADE;
DROP TABLE IF EXISTS participation_query CASCADE;
DROP TABLE IF EXISTS mass_event CASCADE;
DROP TABLE IF EXISTS individual_event CASCADE;
DROP TABLE IF EXISTS user_skill CASCADE;
DROP TABLE IF EXISTS skill CASCADE;
DROP TABLE IF EXISTS user_oauth CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS organisation CASCADE;
DROP TABLE IF EXISTS oauth_providers CASCADE;

-- Удаление типов
DROP TYPE IF EXISTS skill_level CASCADE;
DROP TYPE IF EXISTS participation_status CASCADE;
DROP TYPE IF EXISTS user_role CASCADE;
*/
