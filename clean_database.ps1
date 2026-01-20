# Script to clean the database
# This will drop all tables and clear Liquibase tracking tables
# The database will be recreated by Liquibase on next application startup

$dbHost = "localhost"
$dbPort = "5432"
$dbName = "postgres"
$dbUser = "postgres"
$dbPassword = "12345"
$sqlFile = "sql\clean_database.sql"

Write-Host "Cleaning database: $dbName" -ForegroundColor Yellow
Write-Host "This will drop ALL tables and data!" -ForegroundColor Red
Write-Host ""

# Check if psql is available
$psqlPath = Get-Command psql -ErrorAction SilentlyContinue

if ($psqlPath) {
    Write-Host "Using psql command..." -ForegroundColor Cyan
    $env:PGPASSWORD = $dbPassword
    
    Write-Host "Executing SQL script..." -ForegroundColor Cyan
    Get-Content $sqlFile | psql -h $dbHost -p $dbPort -U $dbUser -d $dbName
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "`nDatabase cleaned successfully!" -ForegroundColor Green
        Write-Host "The database will be recreated by Liquibase on next application startup." -ForegroundColor Green
    } else {
        Write-Host "`nError cleaning database. Exit code: $LASTEXITCODE" -ForegroundColor Red
        Write-Host "Trying alternative method with Docker..." -ForegroundColor Yellow
        
        # Try Docker method
        $dockerContainer = docker ps --filter "ancestor=postgres" --format "{{.Names}}" | Select-Object -First 1
        if ($dockerContainer) {
            Write-Host "Found PostgreSQL container: $dockerContainer" -ForegroundColor Cyan
            Get-Content $sqlFile | docker exec -i $dockerContainer psql -U $dbUser -d $dbName
            if ($LASTEXITCODE -eq 0) {
                Write-Host "`nDatabase cleaned successfully via Docker!" -ForegroundColor Green
            } else {
                Write-Host "`nError: Could not clean database via Docker" -ForegroundColor Red
                exit $LASTEXITCODE
            }
        } else {
            Write-Host "Error: Could not find PostgreSQL container or psql command failed" -ForegroundColor Red
            exit 1
        }
    }
    
    $env:PGPASSWORD = ""
} else {
    Write-Host "psql command not found. Trying Docker method..." -ForegroundColor Yellow
    
    # Try to find PostgreSQL container
    $dockerContainer = docker ps --filter "ancestor=postgres" --format "{{.Names}}" | Select-Object -First 1
    if ($dockerContainer) {
        Write-Host "Found PostgreSQL container: $dockerContainer" -ForegroundColor Cyan
        Write-Host "Executing SQL script via Docker..." -ForegroundColor Cyan
        Get-Content $sqlFile | docker exec -i $dockerContainer psql -U $dbUser -d $dbName
        if ($LASTEXITCODE -eq 0) {
            Write-Host "`nDatabase cleaned successfully via Docker!" -ForegroundColor Green
            Write-Host "The database will be recreated by Liquibase on next application startup." -ForegroundColor Green
        } else {
            Write-Host "`nError: Could not clean database via Docker" -ForegroundColor Red
            exit $LASTEXITCODE
        }
    } else {
        Write-Host "Error: Could not find PostgreSQL container and psql is not in PATH" -ForegroundColor Red
        Write-Host "Please either:" -ForegroundColor Yellow
        Write-Host "  1. Install PostgreSQL client tools and add psql to PATH" -ForegroundColor Yellow
        Write-Host "  2. Or ensure PostgreSQL Docker container is running" -ForegroundColor Yellow
        Write-Host "  3. Or manually execute: sql\clean_database.sql using your preferred SQL client" -ForegroundColor Yellow
        exit 1
    }
}
