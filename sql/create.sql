-- ============================================
-- Скрипт создания базы данных и таблиц
-- Система управления волонтерскими мероприятиями
-- ============================================

-- Создание базы данных
DROP DATABASE IF EXISTS volunteer_system;
CREATE DATABASE volunteer_system
    WITH 
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TEMPLATE = template0;

\c volunteer_system;

-- ============================================
-- СОЗДАНИЕ ТИПОВ
-- ============================================

CREATE TYPE user_role AS ENUM ('user', 'organization_rep', 'admin');
CREATE TYPE participation_status AS ENUM ('pending', 'accepted', 'rejected', 'withdrawn');
CREATE TYPE skill_level AS ENUM ('beginner', 'intermediate', 'advanced', 'expert');

-- ============================================
-- СОЗДАНИЕ ТАБЛИЦ
-- ============================================

-- Таблица: OAuth провайдеры
CREATE TABLE oauth_providers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    CONSTRAINT chk_provider_name_not_empty CHECK (LENGTH(TRIM(name)) > 0)
);

-- Таблица: Организации
CREATE TABLE organisation (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(500),
    CONSTRAINT chk_org_name_not_empty CHECK (LENGTH(TRIM(name)) > 0),
    CONSTRAINT chk_org_address_not_empty CHECK (address IS NULL OR LENGTH(TRIM(address)) > 0)
);

-- Таблица: Пользователи
CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    organisation_id INTEGER,
    role user_role NOT NULL DEFAULT 'user',
    name VARCHAR(255) NOT NULL,
    description TEXT,
    birth_date DATE,
    location VARCHAR(255),
    volunteer_hours REAL NOT NULL DEFAULT 0.0,
    rating REAL DEFAULT 0.0,
    CONSTRAINT fk_user_organisation FOREIGN KEY (organisation_id) 
        REFERENCES organisation(id) ON DELETE SET NULL,
    CONSTRAINT chk_user_name_not_empty CHECK (LENGTH(TRIM(name)) > 0),
    CONSTRAINT chk_volunteer_hours_positive CHECK (volunteer_hours >= 0),
    CONSTRAINT chk_rating_range CHECK (rating >= 0 AND rating <= 5),
    CONSTRAINT chk_birth_date_valid CHECK (birth_date IS NULL OR birth_date <= CURRENT_DATE)
);

-- Таблица: OAuth пользователи
CREATE TABLE user_oauth (
    user_id INTEGER NOT NULL,
    provider_id INTEGER NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    access_token VARCHAR(500) NOT NULL,
    refresh_token VARCHAR(500),
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, provider_id),
    CONSTRAINT fk_oauth_user FOREIGN KEY (user_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_oauth_provider FOREIGN KEY (provider_id) 
        REFERENCES oauth_providers(id) ON DELETE CASCADE,
    CONSTRAINT chk_provider_user_id_not_empty CHECK (LENGTH(TRIM(provider_user_id)) > 0),
    CONSTRAINT chk_access_token_not_empty CHECK (LENGTH(TRIM(access_token)) > 0),
    CONSTRAINT chk_expires_at_future CHECK (expires_at IS NULL OR expires_at > created_at)
);

-- Таблица: Навыки
CREATE TABLE skill (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    CONSTRAINT chk_skill_name_not_empty CHECK (LENGTH(TRIM(name)) > 0)
);

-- Таблица: Навыки пользователей
CREATE TABLE user_skill (
    user_id INTEGER NOT NULL,
    skill_id INTEGER NOT NULL,
    level skill_level NOT NULL DEFAULT 'beginner',
    PRIMARY KEY (user_id, skill_id),
    CONSTRAINT fk_user_skill_user FOREIGN KEY (user_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_skill_skill FOREIGN KEY (skill_id) 
        REFERENCES skill(id) ON DELETE CASCADE
);

-- Таблица: Индивидуальные мероприятия
CREATE TABLE individual_event (
    id SERIAL PRIMARY KEY,
    organiser_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    date_start TIMESTAMP NOT NULL,
    date_end TIMESTAMP NOT NULL,
    volunteers_required INTEGER NOT NULL DEFAULT 1,
    age_restriction INTEGER DEFAULT 0,
    CONSTRAINT fk_individual_event_organiser FOREIGN KEY (organiser_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT chk_individual_title_not_empty CHECK (LENGTH(TRIM(title)) > 0),
    CONSTRAINT chk_individual_date_valid CHECK (date_end > date_start),
    CONSTRAINT chk_individual_volunteers_positive CHECK (volunteers_required > 0),
    CONSTRAINT chk_individual_age_restriction_positive CHECK (age_restriction >= 0)
);

-- Таблица: Массовые мероприятия
CREATE TABLE mass_event (
    id SERIAL PRIMARY KEY,
    organisation_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    date_start TIMESTAMP NOT NULL,
    date_end TIMESTAMP NOT NULL,
    work_hours REAL NOT NULL,
    address VARCHAR(500),
    volunteers_required INTEGER NOT NULL DEFAULT 1,
    age_restriction INTEGER DEFAULT 0,
    CONSTRAINT fk_mass_event_organisation FOREIGN KEY (organisation_id) 
        REFERENCES organisation(id) ON DELETE CASCADE,
    CONSTRAINT chk_mass_title_not_empty CHECK (LENGTH(TRIM(title)) > 0),
    CONSTRAINT chk_mass_date_valid CHECK (date_end > date_start),
    CONSTRAINT chk_mass_work_hours_positive CHECK (work_hours > 0),
    CONSTRAINT chk_mass_volunteers_positive CHECK (volunteers_required > 0),
    CONSTRAINT chk_mass_age_restriction_positive CHECK (age_restriction >= 0),
    CONSTRAINT chk_mass_address_not_empty CHECK (address IS NULL OR LENGTH(TRIM(address)) > 0)
);

-- Таблица: Запросы на участие
CREATE TABLE participation_query (
    event_id INTEGER NOT NULL,
    participant_id INTEGER NOT NULL,
    status participation_status NOT NULL DEFAULT 'pending',
    application_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (event_id, participant_id),
    CONSTRAINT fk_participation_event FOREIGN KEY (event_id) 
        REFERENCES mass_event(id) ON DELETE CASCADE,
    CONSTRAINT fk_participation_participant FOREIGN KEY (participant_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT chk_application_date_valid CHECK (application_date <= CURRENT_TIMESTAMP)
);

-- Таблица: Записи в волонтерскую книжку
CREATE TABLE volunteer_book_entry (
    user_id INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    content TEXT,
    PRIMARY KEY (user_id, event_id),
    CONSTRAINT fk_book_entry_user FOREIGN KEY (user_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_book_entry_event FOREIGN KEY (event_id) 
        REFERENCES individual_event(id) ON DELETE CASCADE
);

-- Таблица: Отзывы
CREATE TABLE review (
    reviewer_id INTEGER NOT NULL,
    reviewed_id INTEGER NOT NULL,
    event_id INTEGER NOT NULL,
    rating REAL NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (reviewer_id, reviewed_id, event_id),
    CONSTRAINT fk_review_reviewer FOREIGN KEY (reviewer_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_reviewed FOREIGN KEY (reviewed_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_event FOREIGN KEY (event_id) 
        REFERENCES individual_event(id) ON DELETE CASCADE,
    CONSTRAINT chk_review_rating_range CHECK (rating >= 1 AND rating <= 5),
    CONSTRAINT chk_review_different_users CHECK (reviewer_id != reviewed_id),
    CONSTRAINT chk_review_created_at_valid CHECK (created_at <= CURRENT_TIMESTAMP)
);

-- Таблица: Таблица лидеров
CREATE TABLE leaderboard (
    user_id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    volunteer_hours REAL NOT NULL DEFAULT 0.0,
    place INTEGER NOT NULL UNIQUE,
    CONSTRAINT fk_leaderboard_user FOREIGN KEY (user_id) 
        REFERENCES "user"(id) ON DELETE CASCADE,
    CONSTRAINT chk_leaderboard_hours_positive CHECK (volunteer_hours >= 0),
    CONSTRAINT chk_leaderboard_place_positive CHECK (place > 0)
);

-- ============================================
-- СОЗДАНИЕ ИНДЕКСОВ
-- ============================================

-- Индексы для повышения производительности
CREATE INDEX idx_user_organisation ON "user"(organisation_id);
CREATE INDEX idx_user_role ON "user"(role);
CREATE INDEX idx_user_volunteer_hours ON "user"(volunteer_hours DESC);
CREATE INDEX idx_user_rating ON "user"(rating DESC);

CREATE INDEX idx_individual_event_organiser ON individual_event(organiser_id);
CREATE INDEX idx_individual_event_date_start ON individual_event(date_start);
CREATE INDEX idx_individual_event_date_end ON individual_event(date_end);

CREATE INDEX idx_mass_event_organisation ON mass_event(organisation_id);
CREATE INDEX idx_mass_event_date_start ON mass_event(date_start);
CREATE INDEX idx_mass_event_date_end ON mass_event(date_end);

CREATE INDEX idx_participation_status ON participation_query(status);
CREATE INDEX idx_participation_participant ON participation_query(participant_id);

CREATE INDEX idx_review_reviewed ON review(reviewed_id);
CREATE INDEX idx_review_rating ON review(rating);

CREATE INDEX idx_oauth_provider_user ON user_oauth(provider_id, provider_user_id);

-- ============================================
-- КОММЕНТАРИИ К ТАБЛИЦАМ
-- ============================================

COMMENT ON TABLE oauth_providers IS 'Список OAuth провайдеров для авторизации';
COMMENT ON TABLE organisation IS 'Организации, проводящие волонтерские мероприятия';
COMMENT ON TABLE "user" IS 'Пользователи системы (волонтеры и представители организаций)';
COMMENT ON TABLE user_oauth IS 'OAuth данные пользователей';
COMMENT ON TABLE skill IS 'Справочник навыков';
COMMENT ON TABLE user_skill IS 'Навыки пользователей с указанием уровня';
COMMENT ON TABLE individual_event IS 'Индивидуальные волонтерские заявки';
COMMENT ON TABLE mass_event IS 'Массовые волонтерские мероприятия от организаций';
COMMENT ON TABLE participation_query IS 'Запросы на участие в массовых мероприятиях';
COMMENT ON TABLE volunteer_book_entry IS 'Записи в волонтерской книжке';
COMMENT ON TABLE review IS 'Отзывы о работе волонтеров';
COMMENT ON TABLE leaderboard IS 'Таблица лидеров по отработанным часам';

COMMENT ON COLUMN "user".volunteer_hours IS 'Общее количество отработанных волонтерских часов';
COMMENT ON COLUMN "user".rating IS 'Средний рейтинг пользователя на основе отзывов';
COMMENT ON COLUMN mass_event.work_hours IS 'Количество часов работы на мероприятии';
COMMENT ON COLUMN participation_query.status IS 'Статус запроса: pending, accepted, rejected, withdrawn';
