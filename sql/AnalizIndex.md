Индексы хороши для чтения, но замедляют вставку, редактирование и удаление. Так же дополнительная расплата в виде дополнительного объема диского пространства

количество индексов нормально если половина от все таблицы

В PostgreSQL автоматически НЕ создаются индексы на FK (в отличие от MySQL/Oracle)! Поэтому важно делать индек для fk если планируем  join
БЕЗ индекса на organisation_id:
1. Берем строку из mass_event (id=1, organisation_id=5)
2. Ищем organisation с id=5
   → PostgreSQL читает ВСЮ таблицу organisation (Full Scan)
3. Повторяем для каждой строки mass_event
примеры с джоином( Список заявок с именем волонтера, Мои запросы на участие, Отзывы о пользователе, OAuth авторизация)



Этап 1: Извлечь SQL-запросы из прецедентов
Для каждого прецедента нужно понять какие запросы будут выполняться:

Прецедент	SQL-запросы	Частота
1. Регистрация	    INSERT INTO "user"	Редко
2. Вход OAuth	    SELECT * FROM user_oauth WHERE provider_id = ? AND provider_user_id = ?	Очень часто
3. Подача запроса	INSERT INTO participation_query	Часто
4. Принятие запроса	SELECT * FROM participation_query WHERE event_id = ? AND status = 'pending'	Часто
5. Отказ от участия	UPDATE participation_query WHERE participant_id = ? AND event_id = ?	Редко
6. Список лидеров	SELECT * FROM "user" ORDER BY volunteer_hours DESC LIMIT 100	Очень часто
7. Отзыв	        INSERT INTO review, SELECT avg(rating) FROM review WHERE reviewed_id = ?	Часто
8. Отстранение	    DELETE FROM participation_query WHERE participant_id = ? AND event_id = ?	Редко
9. Поиск заявок	    SELECT * FROM mass_event WHERE date_start >= ? AND title LIKE ?	Очень часто
10. Роль ПрОрг	    UPDATE "user" SET role = 'organization_rep', organisation_id = ?	Редко
11. Удаление заявки	DELETE FROM mass_event WHERE id = ?	
Редко

### Критичные (выполняются при каждом посещении пользователя)
- OAuth авторизация (прецедент #2)
- Просмотр списка заявок (прецедент #9)
- Модерация запросов (прецедент #4)

### Частые (несколько раз за сессию)
- Личный кабинет (прецедент #6)
- Просмотр профилей (прецедент #7)

### Редкие (не требуют индексов)
- Регистрация, создание/удаление заявок
