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

-- Функция для проверки возраста участника при подаче заявки на массовое мероприятие
CREATE OR REPLACE FUNCTION check_mass_event_age_restriction()
RETURNS TRIGGER AS $$
DECLARE
    participant_birth_date DATE;
    event_age_restriction INTEGER;
    participant_age INTEGER;
BEGIN
    -- Получаем дату рождения участника
    SELECT birth_date INTO participant_birth_date
    FROM "user"
    WHERE id = NEW.participant_id;
    
    -- Получаем возрастное ограничение мероприятия
    SELECT age_restriction INTO event_age_restriction
    FROM mass_event
    WHERE id = NEW.event_id;
    
    -- Если дата рождения не указана, разрешаем участие
    IF participant_birth_date IS NULL THEN
        RETURN NEW;
    END IF;
    
    -- Вычисляем возраст участника
    participant_age := EXTRACT(YEAR FROM AGE(participant_birth_date));
    
    -- Проверяем соответствие возрастному ограничению
    IF participant_age < event_age_restriction THEN
        RAISE EXCEPTION 'Участник не соответствует возрастному ограничению мероприятия (требуется: % лет, возраст участника: % лет)', 
            event_age_restriction, participant_age;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для проверки возраста участника при подаче заявки на индивидуальное мероприятие
CREATE OR REPLACE FUNCTION check_individual_event_age_restriction()
RETURNS TRIGGER AS $$
DECLARE
    participant_birth_date DATE;
    event_age_restriction INTEGER;
    participant_age INTEGER;
    event_start TIMESTAMP;
BEGIN
    -- Получаем дату рождения участника
    SELECT birth_date INTO participant_birth_date
    FROM "user"
    WHERE id = NEW.user_id;
    
    -- Получаем возрастное ограничение и дату начала мероприятия
    SELECT age_restriction, date_start INTO event_age_restriction, event_start
    FROM individual_event
    WHERE id = NEW.event_id;
    
    -- Если дата рождения не указана, разрешаем участие
    IF participant_birth_date IS NULL THEN
        RETURN NEW;
    END IF;
    
    -- Вычисляем возраст участника на момент мероприятия
    participant_age := EXTRACT(YEAR FROM AGE(event_start::DATE, participant_birth_date));
    
    -- Проверяем соответствие возрастному ограничению
    IF participant_age < event_age_restriction THEN
        RAISE EXCEPTION 'Участник не соответствует возрастному ограничению мероприятия (требуется: % лет, возраст участника: % лет)', 
            event_age_restriction, participant_age;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для проверки даты начала массового мероприятия при создании (не старше 1 года)
CREATE OR REPLACE FUNCTION check_mass_event_date_on_insert()
RETURNS TRIGGER AS $$
BEGIN
    -- Проверка только при создании новой записи
    IF TG_OP = 'INSERT' THEN
        IF NEW.date_start < CURRENT_TIMESTAMP - INTERVAL '1 year' THEN
            RAISE EXCEPTION 'Нельзя создавать массовые мероприятия с датой начала старше 1 года назад (дата начала: %, допустимо с: %)', 
                NEW.date_start, (CURRENT_TIMESTAMP - INTERVAL '1 year')::TIMESTAMP;
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для проверки количества участников массового мероприятия
CREATE OR REPLACE FUNCTION check_mass_event_participants_limit()
RETURNS TRIGGER AS $$
DECLARE
    current_participants INTEGER;
    max_participants INTEGER;
BEGIN
    -- Получаем текущее количество принятых участников
    SELECT COUNT(*) INTO current_participants
    FROM participation_query
    WHERE event_id = NEW.event_id AND status = 'accepted';
    
    -- Получаем максимальное количество участников
    SELECT volunteers_required INTO max_participants
    FROM mass_event
    WHERE id = NEW.event_id;
    
    -- Проверяем, не превышен ли лимит при попытке принять заявку
    IF NEW.status = 'accepted' AND current_participants >= max_participants THEN
        RAISE EXCEPTION 'Достигнуто максимальное количество участников для мероприятия (максимум: %, текущее: %)', 
            max_participants, current_participants;
    END IF;
    
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

-- Функция для обновления рейтинга пользователя при добавлении/изменении отзыва
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

-- Функция для автоматического добавления волонтерских часов после завершения мероприятия
CREATE OR REPLACE FUNCTION add_volunteer_hours_on_event_completion()
RETURNS TRIGGER AS $$
DECLARE
    event_work_hours REAL;
    participant_record RECORD;
BEGIN
    -- Проверяем, что мероприятие завершилось
    IF NEW.date_end <= CURRENT_TIMESTAMP AND OLD.date_end > CURRENT_TIMESTAMP THEN
        -- Получаем количество часов мероприятия
        event_work_hours := NEW.work_hours;
        
        -- Добавляем часы всем принятым участникам
        FOR participant_record IN 
            SELECT participant_id 
            FROM participation_query 
            WHERE event_id = NEW.id AND status = 'accepted'
        LOOP
            UPDATE "user"
            SET volunteer_hours = volunteer_hours + event_work_hours
            WHERE id = participant_record.participant_id;
        END LOOP;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для предотвращения удаления мероприятия с принятыми участниками
CREATE OR REPLACE FUNCTION prevent_event_deletion_with_participants()
RETURNS TRIGGER AS $$
DECLARE
    accepted_count INTEGER;
BEGIN
    -- Проверяем наличие принятых участников
    SELECT COUNT(*) INTO accepted_count
    FROM participation_query
    WHERE event_id = OLD.id AND status = 'accepted';
    
    IF accepted_count > 0 THEN
        RAISE EXCEPTION 'Невозможно удалить мероприятие с принятыми участниками (принято участников: %)', accepted_count;
    END IF;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Функция для предотвращения изменения статуса заявки на "withdrawn" менее чем за 24 часа до начала
CREATE OR REPLACE FUNCTION check_withdrawal_deadline()
RETURNS TRIGGER AS $$
DECLARE
    event_start TIMESTAMP;
    hours_until_start REAL;
BEGIN
    -- Проверяем только изменение статуса на 'withdrawn'
    IF NEW.status = 'withdrawn' AND OLD.status != 'withdrawn' THEN
        -- Получаем дату начала мероприятия
        SELECT date_start INTO event_start
        FROM mass_event
        WHERE id = NEW.event_id;
        
        -- Вычисляем количество часов до начала
        hours_until_start := EXTRACT(EPOCH FROM (event_start - CURRENT_TIMESTAMP)) / 3600;
        
        -- Проверяем, что до начала осталось более 24 часов
        IF hours_until_start < 24 THEN
            RAISE EXCEPTION 'Невозможно отказаться от участия менее чем за 24 часа до начала мероприятия (осталось: % часов)', 
                ROUND(hours_until_start::NUMERIC, 2);
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для проверки, что организатор не может участвовать в своем мероприятии
CREATE OR REPLACE FUNCTION prevent_organiser_participation()
RETURNS TRIGGER AS $$
DECLARE
    event_organiser_id INTEGER;
BEGIN
    -- Получаем ID организатора мероприятия
    SELECT organisation_id INTO event_organiser_id
    FROM mass_event
    WHERE id = NEW.event_id;
    
    -- Проверяем, что участник не является представителем организации-организатора
    IF EXISTS (
        SELECT 1 FROM "user" 
        WHERE id = NEW.participant_id 
        AND organisation_id = event_organiser_id
    ) THEN
        RAISE EXCEPTION 'Представитель организации не может подать заявку на участие в мероприятии своей организации';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для проверки, что отзыв оставляется только организатором завершенного мероприятия
CREATE OR REPLACE FUNCTION validate_review_creation()
RETURNS TRIGGER AS $$
DECLARE
    event_organiser_id INTEGER;
    event_end_date TIMESTAMP;
    is_participant BOOLEAN;
BEGIN
    -- Получаем ID организатора и дату окончания мероприятия
    SELECT organiser_id, date_end INTO event_organiser_id, event_end_date
    FROM individual_event
    WHERE id = NEW.event_id;
    
    -- Проверяем, что отзыв оставляет организатор мероприятия
    IF NEW.reviewer_id != event_organiser_id THEN
        RAISE EXCEPTION 'Только организатор мероприятия может оставлять отзывы';
    END IF;
    
    -- Проверяем, что мероприятие завершилось
    IF event_end_date > CURRENT_TIMESTAMP THEN
        RAISE EXCEPTION 'Отзывы можно оставлять только после завершения мероприятия';
    END IF;
    
    -- Проверяем, что пользователь действительно участвовал в мероприятии
    SELECT EXISTS (
        SELECT 1 FROM volunteer_book_entry 
        WHERE user_id = NEW.reviewed_id AND event_id = NEW.event_id
    ) INTO is_participant;
    
    IF NOT is_participant THEN
        RAISE EXCEPTION 'Отзыв можно оставить только участнику мероприятия';
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Функция для автоматического создания записи в волонтерской книжке после завершения индивидуального мероприятия
CREATE OR REPLACE FUNCTION create_volunteer_book_entry_on_completion()
RETURNS TRIGGER AS $$
BEGIN
    -- Проверяем, что мероприятие только что завершилось
    IF NEW.date_end <= CURRENT_TIMESTAMP AND OLD.date_end > CURRENT_TIMESTAMP THEN
        -- Создаем записи в волонтерской книжке для всех участников
        -- (предполагается, что участники добавляются отдельным механизмом)
        -- Эта функция служит для контроля целостности
        NULL;
    END IF;
    
    RETURN NEW;
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

-- Функция для проверки даты начала мероприятия при создании записи
CREATE OR REPLACE FUNCTION check_event_date_on_insert()
RETURNS TRIGGER AS $$
BEGIN
    -- Проверка только при создании новой записи
    IF TG_OP = 'INSERT' THEN
        IF NEW.date_start < CURRENT_TIMESTAMP - INTERVAL '1 month' THEN
            RAISE EXCEPTION 'Нельзя создавать мероприятия старше 1 месяца назад';
        END IF;
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

-- Триггер для проверки возраста при подаче заявки на массовое мероприятие
CREATE TRIGGER trg_check_mass_event_age
    BEFORE INSERT OR UPDATE ON participation_query
    FOR EACH ROW
    EXECUTE FUNCTION check_mass_event_age_restriction();

-- Триггер для проверки возраста при создании записи в волонтерской книжке
CREATE TRIGGER trg_check_individual_event_age
    BEFORE INSERT ON volunteer_book_entry
    FOR EACH ROW
    EXECUTE FUNCTION check_individual_event_age_restriction();

-- Триггер для проверки даты начала при создании массового мероприятия
CREATE TRIGGER trg_check_mass_event_date_on_insert
    BEFORE INSERT ON mass_event
    FOR EACH ROW
    EXECUTE FUNCTION check_mass_event_date_on_insert();

-- Триггер для проверки лимита участников при принятии заявки
CREATE TRIGGER trg_check_participants_limit
    BEFORE UPDATE ON participation_query
    FOR EACH ROW
    WHEN (NEW.status = 'accepted' AND OLD.status != 'accepted')
    EXECUTE FUNCTION check_mass_event_participants_limit();

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

-- Триггер для обновления рейтинга при изменении отзыва
CREATE TRIGGER trg_update_rating_on_update
    AFTER UPDATE ON review
    FOR EACH ROW
    EXECUTE FUNCTION update_user_rating();

-- Триггер для обновления рейтинга при удалении отзыва
CREATE TRIGGER trg_update_rating_on_delete
    AFTER DELETE ON review
    FOR EACH ROW
    EXECUTE FUNCTION update_user_rating();

-- Триггер для добавления волонтерских часов после завершения массового мероприятия
CREATE TRIGGER trg_add_volunteer_hours
    AFTER UPDATE OF date_end ON mass_event
    FOR EACH ROW
    EXECUTE FUNCTION add_volunteer_hours_on_event_completion();

-- Триггер для предотвращения удаления мероприятия с участниками
CREATE TRIGGER trg_prevent_event_deletion
    BEFORE DELETE ON mass_event
    FOR EACH ROW
    EXECUTE FUNCTION prevent_event_deletion_with_participants();

-- Триггер для проверки срока отказа от участия
CREATE TRIGGER trg_check_withdrawal_deadline
    BEFORE UPDATE ON participation_query
    FOR EACH ROW
    EXECUTE FUNCTION check_withdrawal_deadline();

-- Триггер для предотвращения участия организатора в своем мероприятии
CREATE TRIGGER trg_prevent_organiser_participation
    BEFORE INSERT ON participation_query
    FOR EACH ROW
    EXECUTE FUNCTION prevent_organiser_participation();

-- Триггер для валидации создания отзыва
CREATE TRIGGER trg_validate_review
    BEFORE INSERT ON review
    FOR EACH ROW
    EXECUTE FUNCTION validate_review_creation();

-- Триггер для синхронизации имени в таблице лидеров
CREATE TRIGGER trg_sync_leaderboard_name
    AFTER UPDATE OF name ON "user"
    FOR EACH ROW
    EXECUTE FUNCTION sync_leaderboard_name();

-- Триггер для проверки даты начала мероприятия при создании записи
CREATE TRIGGER trg_check_individual_event_date
    BEFORE INSERT ON individual_event
    FOR EACH ROW
    EXECUTE FUNCTION check_event_date_on_insert();

-- Удален дублирующий триггер trg_check_mass_event_date (используется новый trg_check_mass_event_date_on_insert)

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
