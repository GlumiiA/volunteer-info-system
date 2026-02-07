-- ============================================
-- Скрипт создания триггеров для обеспечения целостности данных
-- Система управления волонтерскими мероприятиями
-- ============================================

\c volunteer_system;

-- ============================================
-- ФУНКЦИИ ДЛЯ ТРИГГЕРОВ
-- ============================================

-- Функция для обновления timestamp при изменении OAuth данных
CREATE OR REPLACE FUNCTION update_oauth_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для обновления таблицы лидеров при изменении волонтерских часов
CREATE OR REPLACE FUNCTION update_leaderboard()
RETURNS TRIGGER AS $$
BEGIN
    -- Удаляем старую запись если она есть
    DELETE FROM leaderboard WHERE user_id = NEW.id;
    
    -- Добавляем новую запись только если есть отработанные часы
    IF NEW.volunteer_hours > 0 THEN
        INSERT INTO leaderboard (user_id, name, volunteer_hours, place)
        VALUES (NEW.id, NEW.name, NEW.volunteer_hours, 0);
        
        -- Обновляем места всех участников
        WITH ranked_users AS (
            SELECT user_id, ROW_NUMBER() OVER (ORDER BY volunteer_hours DESC, user_id) AS new_place
            FROM leaderboard
        )
        UPDATE leaderboard
        SET place = ranked_users.new_place
        FROM ranked_users
        WHERE leaderboard.user_id = ranked_users.user_id;
    ELSE
        -- Пересчитываем места для оставшихся участников
        WITH ranked_users AS (
            SELECT user_id, ROW_NUMBER() OVER (ORDER BY volunteer_hours DESC, user_id) AS new_place
            FROM leaderboard
        )
        UPDATE leaderboard
        SET place = ranked_users.new_place
        FROM ranked_users
        WHERE leaderboard.user_id = ranked_users.user_id;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для обновления рейтинга пользователя при добавлении отзыва
CREATE OR REPLACE FUNCTION update_user_rating()
RETURNS TRIGGER AS $$
DECLARE
    avg_rating REAL;
BEGIN
    -- Вычисляем средний рейтинг для пользователя
    SELECT AVG(rating) INTO avg_rating
    FROM review
    WHERE reviewed_id = COALESCE(NEW.reviewed_id, OLD.reviewed_id);
    
    -- Обновляем рейтинг пользователя
    UPDATE "user"
    SET rating = COALESCE(avg_rating, 0)
    WHERE id = COALESCE(NEW.reviewed_id, OLD.reviewed_id);
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Функция для обновления имени в таблице лидеров при изменении имени пользователя
CREATE OR REPLACE FUNCTION sync_leaderboard_name()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.name != OLD.name THEN
        UPDATE leaderboard
        SET name = NEW.name
        WHERE user_id = NEW.id;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ============================================
-- СОЗДАНИЕ ТРИГГЕРОВ
-- ============================================

-- Триггер для обновления timestamp OAuth данных
CREATE TRIGGER trg_update_oauth_timestamp
    BEFORE UPDATE ON user_oauth
    FOR EACH ROW
    EXECUTE FUNCTION update_oauth_timestamp();

-- Триггер для обновления таблицы лидеров
CREATE TRIGGER trg_update_leaderboard
    AFTER UPDATE OF volunteer_hours ON "user"
    FOR EACH ROW
    WHEN (NEW.volunteer_hours != OLD.volunteer_hours)
    EXECUTE FUNCTION update_leaderboard();

-- Триггер для обновления рейтинга при добавлении отзыва
CREATE TRIGGER trg_update_rating_on_insert
    AFTER INSERT ON review
    FOR EACH ROW
    EXECUTE FUNCTION update_user_rating();

-- Триггер для обновления рейтинга при удалении отзыва
CREATE TRIGGER trg_update_rating_on_delete
    AFTER DELETE ON review
    FOR EACH ROW
    EXECUTE FUNCTION update_user_rating();

-- Триггер для синхронизации имени в таблице лидеров
CREATE TRIGGER trg_sync_leaderboard_name
    AFTER UPDATE OF name ON "user"
    FOR EACH ROW
    EXECUTE FUNCTION sync_leaderboard_name();

-- ============================================
-- ИНФОРМАЦИЯ О СОЗДАННЫХ ТРИГГЕРАХ
-- ============================================

-- Вывод списка всех триггеров
SELECT 
    trigger_name,
    event_manipulation,
    event_object_table,
    action_timing,
    action_statement
FROM information_schema.triggers
WHERE trigger_schema = 'public'
ORDER BY event_object_table, trigger_name;

COMMENT ON FUNCTION update_oauth_timestamp() IS 'Автоматическое обновление времени изменения OAuth данных';
COMMENT ON FUNCTION check_mass_event_age_restriction() IS 'Проверка соответствия возраста участника ограничениям массового мероприятия';
COMMENT ON FUNCTION check_individual_event_age_restriction() IS 'Проверка соответствия возраста участника ограничениям индивидуального мероприятия';
COMMENT ON FUNCTION check_mass_event_date_on_insert() IS 'Проверка даты начала массового мероприятия при создании (не старше 1 года)';
COMMENT ON FUNCTION check_mass_event_participants_limit() IS 'Проверка лимита участников массового мероприятия';
COMMENT ON FUNCTION update_leaderboard() IS 'Автоматическое обновление таблицы лидеров при изменении волонтерских часов';
COMMENT ON FUNCTION update_user_rating() IS 'Автоматический пересчет рейтинга пользователя на основе отзывов';
COMMENT ON FUNCTION add_volunteer_hours_on_event_completion() IS 'Автоматическое добавление волонтерских часов после завершения мероприятия';
COMMENT ON FUNCTION prevent_event_deletion_with_participants() IS 'Предотвращение удаления мероприятия с принятыми участниками';
COMMENT ON FUNCTION check_withdrawal_deadline() IS 'Проверка возможности отказа от участия (не менее чем за 24 часа)';
COMMENT ON FUNCTION prevent_organiser_participation() IS 'Предотвращение участия организатора в собственном мероприятии';
COMMENT ON FUNCTION validate_review_creation() IS 'Валидация создания отзыва (только организатором после завершения)';
COMMENT ON FUNCTION sync_leaderboard_name() IS 'Синхронизация имени пользователя в таблице лидеров';
COMMENT ON FUNCTION check_event_date_on_insert() IS 'Проверка даты начала индивидуального мероприятия при создании (не старше 1 месяца)';
