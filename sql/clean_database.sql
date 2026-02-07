-- ============================================
-- Script to clean the database
-- This will drop all tables and clear Liquibase tracking tables
-- The database will be recreated by Liquibase on next application startup
-- ============================================

-- Disconnect all active connections
SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = current_database()
  AND pid <> pg_backend_pid();

-- Drop all tables in correct order (respecting foreign key constraints)
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS volunteer_book_entries CASCADE;
DROP TABLE IF EXISTS participations CASCADE;
DROP TABLE IF EXISTS mass_events CASCADE;
DROP TABLE IF EXISTS individual_events CASCADE;
DROP TABLE IF EXISTS organization_role_requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS organisations CASCADE;

-- Drop Liquibase tracking tables
DROP TABLE IF EXISTS databasechangelog CASCADE;
DROP TABLE IF EXISTS databasechangeloglock CASCADE;

-- Drop any sequences that might exist
DROP SEQUENCE IF EXISTS organisations_id_seq CASCADE;
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
DROP SEQUENCE IF EXISTS individual_events_id_seq CASCADE;
DROP SEQUENCE IF EXISTS mass_events_id_seq CASCADE;
DROP SEQUENCE IF EXISTS reviews_id_seq CASCADE;
DROP SEQUENCE IF EXISTS volunteer_book_entries_id_seq CASCADE;
DROP SEQUENCE IF EXISTS participations_id_seq CASCADE;
DROP SEQUENCE IF EXISTS organization_role_requests_id_seq CASCADE;

-- Verify cleanup
SELECT 'Database cleaned successfully! All tables and sequences have been dropped.' AS status;
