# Database Cleaning Instructions

To clean the database and remove all data (useful for fixing data corruption issues):

## Option 1: Using PowerShell Script (Recommended)

Run the PowerShell script:
```powershell
.\clean_database.ps1
```

The script will:
1. Drop all tables in the correct order
2. Clear Liquibase tracking tables
3. Drop all sequences

After running, restart your backend application and Liquibase will recreate all tables automatically.

## Option 2: Using SQL File Directly

If you have direct access to PostgreSQL (via psql or a database client):

```bash
psql -h localhost -p 5432 -U postgres -d postgres -f sql/clean_database.sql
```

Or if using Docker:
```bash
docker exec -i <postgres_container_name> psql -U postgres -d postgres < sql/clean_database.sql
```

## Option 3: Manual SQL Execution

Open `sql/clean_database.sql` in your preferred database client (pgAdmin, DBeaver, etc.) and execute it.

## After Cleaning

1. Stop your backend application if it's running
2. Run the clean script
3. Start your backend application
4. Liquibase will automatically recreate all tables with the latest schema

## Database Connection Details

- Host: localhost
- Port: 5432
- Database: postgres
- Username: postgres
- Password: 12345

(These are configured in `backend/src/main/resources/application.properties`)
