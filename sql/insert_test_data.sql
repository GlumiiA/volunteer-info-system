-- ============================================
-- Скрипт заполнения базы данных тестовыми данными
-- Система управления волонтерскими мероприятиями
-- ============================================

\c volunteer_system;

-- ============================================
-- ОЧИСТКА СУЩЕСТВУЮЩИХ ДАННЫХ
-- ============================================

TRUNCATE TABLE leaderboard CASCADE;
TRUNCATE TABLE review CASCADE;
TRUNCATE TABLE volunteer_book_entry CASCADE;
TRUNCATE TABLE participation_query CASCADE;
TRUNCATE TABLE mass_event CASCADE;
TRUNCATE TABLE individual_event CASCADE;
TRUNCATE TABLE user_skill CASCADE;
TRUNCATE TABLE skill CASCADE;
TRUNCATE TABLE user_oauth CASCADE;
TRUNCATE TABLE "user" CASCADE;
TRUNCATE TABLE organisation CASCADE;
TRUNCATE TABLE oauth_providers CASCADE;

-- Сброс последовательностей
ALTER SEQUENCE oauth_providers_id_seq RESTART WITH 1;
ALTER SEQUENCE organisation_id_seq RESTART WITH 1;
ALTER SEQUENCE user_id_seq RESTART WITH 1;
ALTER SEQUENCE skill_id_seq RESTART WITH 1;
ALTER SEQUENCE individual_event_id_seq RESTART WITH 1;
ALTER SEQUENCE mass_event_id_seq RESTART WITH 1;

-- ============================================
-- ВСТАВКА ТЕСТОВЫХ ДАННЫХ
-- ============================================

-- OAuth провайдеры
INSERT INTO oauth_providers (name) VALUES
('Google'),
('GitHub'),
('VK'),
('Yandex');

-- Организации
INSERT INTO organisation (name, description, address) VALUES
('Благотворительный фонд "Доброе сердце"', 
 'Помощь нуждающимся семьям и детям', 
 'г. Санкт-Петербург, ул. Ленина, д. 15'),
('Экологическое движение "Чистый город"', 
 'Защита окружающей среды и озеленение города', 
 'г. Санкт-Петербург, пр. Просвещения, д. 45'),
('Фонд помощи животным "Верный друг"', 
 'Помощь бездомным животным и организация приютов', 
 'г. Санкт-Петербург, ул. Маршала Захарова, д. 8'),
('Молодежный волонтерский центр "Инициатива"', 
 'Развитие волонтерского движения среди молодежи', 
 'г. Санкт-Петербург, Кронверкский пр., д. 49'),
('Культурный центр "Наследие"', 
 'Сохранение культурного наследия и организация мероприятий', 
 'г. Санкт-Петербург, Невский пр., д. 30');

-- Пользователи
INSERT INTO "user" (organisation_id, role, name, description, birth_date, location, volunteer_hours, rating) VALUES
-- Администратор
(NULL, 'admin', 'Иванов Иван Иванович', 'Администратор системы', '1985-03-15', 'Санкт-Петербург', 0, 5.0),

-- Представители организаций
(1, 'organization_rep', 'Петрова Мария Сергеевна', 'Координатор волонтерских программ', '1990-07-22', 'Санкт-Петербург', 150, 4.9),
(2, 'organization_rep', 'Сидоров Алексей Викторович', 'Руководитель экологических проектов', '1988-11-30', 'Санкт-Петербург', 200, 4.8),
(3, 'organization_rep', 'Козлова Елена Дмитриевна', 'Менеджер приютов для животных', '1992-05-18', 'Санкт-Петербург', 180, 4.7),
(4, 'organization_rep', 'Морозов Дмитрий Александрович', 'Лидер молодежных программ', '1995-09-10', 'Санкт-Петербург', 220, 4.9),

-- Обычные волонтеры
(NULL, 'user', 'Смирнова Анна Павловна', 'Люблю помогать людям', '2000-01-15', 'Санкт-Петербург', 45, 4.5),
(NULL, 'user', 'Кузнецов Максим Олегович', 'Интересуюсь экологией', '1998-06-20', 'Санкт-Петербург', 78, 4.6),
(NULL, 'user', 'Новикова Ольга Ивановна', 'Помогаю животным в приютах', '2001-12-05', 'Санкт-Петербург', 62, 4.4),
(NULL, 'user', 'Лебедев Артем Сергеевич', 'Участвую в культурных мероприятиях', '1999-03-28', 'Санкт-Петербург', 91, 4.7),
(NULL, 'user', 'Волкова Татьяна Николаевна', 'Организую субботники', '2002-08-14', 'Санкт-Петербург', 55, 4.3),
(NULL, 'user', 'Соколов Игорь Владимирович', 'Помогаю пожилым людям', '1997-04-07', 'Санкт-Петербург', 120, 4.8),
(NULL, 'user', 'Павлова Екатерина Андреевна', 'Работаю с детьми', '2000-10-22', 'Санкт-Петербург', 85, 4.6),
(NULL, 'user', 'Федоров Никита Денисович', 'Увлекаюсь спортивным волонтерством', '1998-02-11', 'Санкт-Петербург', 70, 4.5),
(NULL, 'user', 'Орлова Виктория Михайловна', 'Помогаю в библиотеках', '2001-07-30', 'Санкт-Петербург', 40, 4.2),
(NULL, 'user', 'Егоров Денис Петрович', 'Участвую в благоустройстве города', '1999-11-18', 'Санкт-Петербург', 95, 4.7);

-- OAuth данные пользователей
INSERT INTO user_oauth (user_id, provider_id, provider_user_id, access_token, refresh_token, expires_at) VALUES
(1, 1, 'google_admin_123', 'access_token_admin_xyz', 'refresh_token_admin_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(2, 1, 'google_user_234', 'access_token_user1_xyz', 'refresh_token_user1_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(3, 2, 'github_user_345', 'access_token_user2_xyz', 'refresh_token_user2_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(4, 1, 'google_user_456', 'access_token_user3_xyz', 'refresh_token_user3_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(5, 3, 'vk_user_567', 'access_token_user4_xyz', 'refresh_token_user4_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(6, 1, 'google_user_678', 'access_token_user5_xyz', 'refresh_token_user5_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(7, 2, 'github_user_789', 'access_token_user6_xyz', 'refresh_token_user6_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(8, 1, 'google_user_890', 'access_token_user7_xyz', 'refresh_token_user7_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(9, 4, 'yandex_user_901', 'access_token_user8_xyz', 'refresh_token_user8_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(10, 1, 'google_user_012', 'access_token_user9_xyz', 'refresh_token_user9_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(11, 2, 'github_user_123', 'access_token_user10_xyz', 'refresh_token_user10_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(12, 1, 'google_user_234', 'access_token_user11_xyz', 'refresh_token_user11_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(13, 3, 'vk_user_345', 'access_token_user12_xyz', 'refresh_token_user12_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(14, 1, 'google_user_456', 'access_token_user13_xyz', 'refresh_token_user13_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days'),
(15, 2, 'github_user_567', 'access_token_user14_xyz', 'refresh_token_user14_xyz', CURRENT_TIMESTAMP + INTERVAL '7 days');

-- Навыки
INSERT INTO skill (name, description) VALUES
('Организация мероприятий', 'Умение планировать и проводить различные мероприятия'),
('Работа с детьми', 'Опыт взаимодействия с детьми разных возрастов'),
('Уход за животными', 'Навыки ухода за домашними и бездомными животными'),
('Садоводство', 'Знания в области озеленения и ухода за растениями'),
('Уборка территорий', 'Опыт проведения субботников и благоустройства'),
('Первая помощь', 'Навыки оказания первой медицинской помощи'),
('Фотография', 'Умение фотографировать мероприятия'),
('Работа с пожилыми людьми', 'Опыт помощи и общения с пожилыми людьми'),
('Иностранные языки', 'Владение иностранными языками для работы с иностранцами'),
('IT-навыки', 'Компьютерная грамотность и навыки работы с техникой'),
('Кулинария', 'Умение готовить для массовых мероприятий'),
('Строительство', 'Навыки ремонта и строительства');

-- Навыки пользователей
INSERT INTO user_skill (user_id, skill_id, level) VALUES
-- Представители организаций имеют высокий уровень навыков
(2, 1, 'expert'), (2, 2, 'advanced'), (2, 6, 'advanced'),
(3, 5, 'expert'), (3, 4, 'advanced'), (3, 12, 'intermediate'),
(4, 3, 'expert'), (4, 6, 'advanced'),
(5, 1, 'advanced'), (5, 2, 'expert'), (5, 7, 'intermediate'),

-- Волонтеры
(6, 2, 'intermediate'), (6, 6, 'beginner'),
(7, 5, 'advanced'), (7, 4, 'intermediate'),
(8, 3, 'advanced'), (8, 6, 'intermediate'),
(9, 1, 'intermediate'), (9, 7, 'advanced'),
(10, 5, 'intermediate'), (10, 12, 'beginner'),
(11, 8, 'advanced'), (11, 6, 'intermediate'),
(12, 2, 'advanced'), (12, 9, 'intermediate'),
(13, 10, 'advanced'), (13, 7, 'intermediate'),
(14, 11, 'intermediate'), (14, 1, 'beginner'),
(15, 5, 'advanced'), (15, 12, 'intermediate');

-- Массовые мероприятия
INSERT INTO mass_event (organisation_id, title, description, date_start, date_end, work_hours, address, volunteers_required, age_restriction) VALUES
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
 16),
 
(4, 'Организация молодежного форума', 
 'Помощь в проведении молодежного форума, регистрация участников', 
 CURRENT_TIMESTAMP + INTERVAL '20 days', 
 CURRENT_TIMESTAMP + INTERVAL '22 days', 
 16.0, 
 'г. Санкт-Петербург, Кронверкский пр., д. 49', 
 25, 
 18),
 
(5, 'Фестиваль культурного наследия', 
 'Помощь в организации культурного фестиваля, работа на площадках', 
 CURRENT_TIMESTAMP + INTERVAL '30 days', 
 CURRENT_TIMESTAMP + INTERVAL '32 days', 
 20.0, 
 'г. Санкт-Петербург, Дворцовая площадь', 
 40, 
 16),

-- Прошедшие мероприятия
(1, 'Новогодний праздник для детей', 
 'Организация новогоднего праздника', 
 CURRENT_TIMESTAMP - INTERVAL '30 days', 
 CURRENT_TIMESTAMP - INTERVAL '30 days' + INTERVAL '6 hours', 
 6.0, 
 'г. Санкт-Петербург, ул. Праздничная, д. 1', 
 20, 
 18),
 
(2, 'Весенний субботник', 
 'Уборка города после зимы', 
 CURRENT_TIMESTAMP - INTERVAL '15 days', 
 CURRENT_TIMESTAMP - INTERVAL '15 days' + INTERVAL '4 hours', 
 4.0, 
 'г. Санкт-Петербург, различные районы', 
 50, 
 14);

-- Индивидуальные мероприятия
INSERT INTO individual_event (organiser_id, title, description, date_start, date_end, volunteers_required, age_restriction) VALUES
-- Будущие заявки
(11, 'Помощь в переезде пожилой соседке', 
 'Требуется помощь в упаковке вещей и переносе мебели', 
 CURRENT_TIMESTAMP + INTERVAL '3 days', 
 CURRENT_TIMESTAMP + INTERVAL '3 days' + INTERVAL '4 hours', 
 2, 
 18),
 
(6, 'Присмотр за домашними животными', 
 'Необходим присмотр за двумя кошками во время отъезда', 
 CURRENT_TIMESTAMP + INTERVAL '7 days', 
 CURRENT_TIMESTAMP + INTERVAL '14 days', 
 1, 
 16),
 
(12, 'Помощь в организации детского праздника', 
 'Нужны аниматоры и помощники для проведения детского дня рождения', 
 CURRENT_TIMESTAMP + INTERVAL '12 days', 
 CURRENT_TIMESTAMP + INTERVAL '12 days' + INTERVAL '3 hours', 
 3, 
 18),
 
(10, 'Уборка придомовой территории', 
 'Совместная уборка двора и посадка цветов', 
 CURRENT_TIMESTAMP + INTERVAL '18 days', 
 CURRENT_TIMESTAMP + INTERVAL '18 days' + INTERVAL '3 hours', 
 5, 
 14),

-- Прошедшие заявки
(8, 'Ремонт в квартире пожилого человека', 
 'Требовалась помощь в мелком ремонте', 
 CURRENT_TIMESTAMP - INTERVAL '20 days', 
 CURRENT_TIMESTAMP - INTERVAL '20 days' + INTERVAL '5 hours', 
 2, 
 18),
 
(9, 'Помощь в саду', 
 'Обрезка деревьев и уборка листвы', 
 CURRENT_TIMESTAMP - INTERVAL '10 days', 
 CURRENT_TIMESTAMP - INTERVAL '10 days' + INTERVAL '4 hours', 
 3, 
 16);

-- Запросы на участие в массовых мероприятиях
INSERT INTO participation_query (event_id, participant_id, status, application_date) VALUES
-- Будущие мероприятия
(1, 6, 'accepted', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(1, 7, 'accepted', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(1, 8, 'pending', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(1, 9, 'accepted', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(1, 10, 'rejected', CURRENT_TIMESTAMP - INTERVAL '3 days'),

(2, 6, 'accepted', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(2, 7, 'accepted', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(2, 11, 'accepted', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(2, 12, 'accepted', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(2, 13, 'pending', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(2, 14, 'accepted', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(2, 15, 'accepted', CURRENT_TIMESTAMP - INTERVAL '2 days'),

(3, 8, 'accepted', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(3, 9, 'accepted', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(3, 10, 'accepted', CURRENT_TIMESTAMP - INTERVAL '4 days'),

(4, 6, 'pending', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 7, 'pending', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 12, 'accepted', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(4, 13, 'accepted', CURRENT_TIMESTAMP - INTERVAL '3 days'),

(5, 9, 'pending', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(5, 11, 'pending', CURRENT_TIMESTAMP - INTERVAL '2 days'),

-- Прошедшие мероприятия
(6, 6, 'accepted', CURRENT_TIMESTAMP - INTERVAL '35 days'),
(6, 7, 'accepted', CURRENT_TIMESTAMP - INTERVAL '35 days'),
(6, 11, 'accepted', CURRENT_TIMESTAMP - INTERVAL '34 days'),
(6, 12, 'accepted', CURRENT_TIMESTAMP - INTERVAL '34 days'),

(7, 7, 'accepted', CURRENT_TIMESTAMP - INTERVAL '20 days'),
(7, 9, 'accepted', CURRENT_TIMESTAMP - INTERVAL '20 days'),
(7, 10, 'accepted', CURRENT_TIMESTAMP - INTERVAL '20 days'),
(7, 15, 'accepted', CURRENT_TIMESTAMP - INTERVAL '19 days');

-- Записи в волонтерскую книжку (только для завершенных индивидуальных мероприятий)
INSERT INTO volunteer_book_entry (user_id, event_id, content) VALUES
(11, 5, 'Помог с ремонтом квартиры. Выполнил покраску стен и мелкий ремонт мебели.'),
(15, 5, 'Участвовал в ремонте. Помогал с демонтажем старых обоев и подготовкой поверхностей.'),
(7, 6, 'Помогал в саду. Обрезал деревья и убрал листву во дворе.'),
(10, 6, 'Участвовал в садовых работах. Помог с обрезкой кустарников.'),
(14, 6, 'Работал в саду. Убирал листву и подготавливал почву для посадки.');

-- Отзывы (только для завершенных индивидуальных мероприятий)
INSERT INTO review (reviewer_id, reviewed_id, event_id, rating, content, created_at) VALUES
(8, 11, 5, 5.0, 'Отличный волонтер! Работал очень аккуратно и профессионально.', CURRENT_TIMESTAMP - INTERVAL '19 days'),
(8, 15, 5, 4.5, 'Хорошо справился с задачей, старательный и ответственный.', CURRENT_TIMESTAMP - INTERVAL '19 days'),
(9, 7, 6, 5.0, 'Большое спасибо за помощь! Очень качественная работа.', CURRENT_TIMESTAMP - INTERVAL '9 days'),
(9, 10, 6, 4.0, 'Хорошо помог, но немного медленно работал.', CURRENT_TIMESTAMP - INTERVAL '9 days'),
(9, 14, 6, 4.5, 'Отличный помощник, всё сделал как нужно.', CURRENT_TIMESTAMP - INTERVAL '9 days');

-- Таблица лидеров будет заполнена триггером
-- Но для тестирования можно добавить начальные данные:
INSERT INTO leaderboard (user_id, name, volunteer_hours, place) VALUES
(5, 'Морозов Дмитрий Александрович', 220, 1),
(3, 'Сидоров Алексей Викторович', 200, 2),
(4, 'Козлова Елена Дмитриевна', 180, 3),
(2, 'Петрова Мария Сергеевна', 150, 4),
(11, 'Соколов Игорь Владимирович', 120, 5),
(15, 'Егоров Денис Петрович', 95, 6),
(9, 'Лебедев Артем Сергеевич', 91, 7),
(12, 'Павлова Екатерина Андреевна', 85, 8),
(7, 'Кузнецов Максим Олегович', 78, 9),
(13, 'Федоров Никита Денисович', 70, 10),
(8, 'Новикова Ольга Ивановна', 62, 11),
(10, 'Волкова Татьяна Николаевна', 55, 12),
(6, 'Смирнова Анна Павловна', 45, 13),
(14, 'Орлова Виктория Михайловна', 40, 14);

-- ============================================
-- ПРОВЕРКА ВСТАВЛЕННЫХ ДАННЫХ
-- ============================================

-- Вывод статистики
SELECT 'OAuth провайдеры' AS table_name, COUNT(*) AS count FROM oauth_providers
UNION ALL
SELECT 'Организации', COUNT(*) FROM organisation
UNION ALL
SELECT 'Пользователи', COUNT(*) FROM "user"
UNION ALL
SELECT 'OAuth пользователи', COUNT(*) FROM user_oauth
UNION ALL
SELECT 'Навыки', COUNT(*) FROM skill
UNION ALL
SELECT 'Навыки пользователей', COUNT(*) FROM user_skill
UNION ALL
SELECT 'Массовые мероприятия', COUNT(*) FROM mass_event
UNION ALL
SELECT 'Индивидуальные мероприятия', COUNT(*) FROM individual_event
UNION ALL
SELECT 'Запросы на участие', COUNT(*) FROM participation_query
UNION ALL
SELECT 'Записи в волонтерскую книжку', COUNT(*) FROM volunteer_book_entry
UNION ALL
SELECT 'Отзывы', COUNT(*) FROM review
UNION ALL
SELECT 'Таблица лидеров', COUNT(*) FROM leaderboard;
