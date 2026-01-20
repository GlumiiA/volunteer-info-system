# Script to set up admin account
# This will register the admin user and update their role

$apiUrl = "http://localhost:8080/api/v1"
$adminEmail = "admin@volunteer-system.local"
$adminPassword = "admin123"
$adminName = "Администратор"

Write-Host "Setting up admin account..." -ForegroundColor Yellow
Write-Host "Email: $adminEmail" -ForegroundColor Cyan
Write-Host "Password: $adminPassword" -ForegroundColor Cyan
Write-Host ""

# Check if backend is running
Write-Host "Checking if backend is running..." -ForegroundColor Cyan
try {
    $testBody = @{email="test";password="test"} | ConvertTo-Json
    $null = Invoke-WebRequest -Uri "$apiUrl/auth/login" -Method Post -Body $testBody -ContentType "application/json" -ErrorAction Stop -TimeoutSec 3
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    if ($statusCode -ne 400 -and $statusCode -ne 401) {
        Write-Host "Backend is not running or not accessible!" -ForegroundColor Red
        Write-Host "Please start the backend first:" -ForegroundColor Yellow
        Write-Host "  cd backend" -ForegroundColor White
        Write-Host "  .\gradlew.bat bootRun" -ForegroundColor White
        Write-Host ""
        Write-Host "Then run this script again." -ForegroundColor Yellow
        exit 1
    }
    # 400 or 401 means backend is running (just wrong credentials)
    Write-Host "✓ Backend is running" -ForegroundColor Green
}

# Step 1: Try to register the admin user
Write-Host "Step 1: Registering admin user..." -ForegroundColor Yellow
$registerBody = @{
    email = $adminEmail
    password = $adminPassword
    fullName = $adminName
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$apiUrl/auth/register" -Method Post -Body $registerBody -ContentType "application/json" -ErrorAction Stop
    Write-Host "✓ User registered successfully!" -ForegroundColor Green
    $token = $registerResponse.token
    $userId = $registerResponse.user.id
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "User already exists. Attempting to login..." -ForegroundColor Yellow
        # Try to login
        $loginBody = @{
            email = $adminEmail
            password = $adminPassword
        } | ConvertTo-Json
        
        try {
            $loginResponse = Invoke-RestMethod -Uri "$apiUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json" -ErrorAction Stop
            $token = $loginResponse.token
            $userId = $loginResponse.user.id
            Write-Host "✓ Logged in successfully!" -ForegroundColor Green
            
            if ($loginResponse.user.role -eq "ADMIN") {
                Write-Host "✓ User is already an admin!" -ForegroundColor Green
                Write-Host ""
                Write-Host "Admin account is ready to use!" -ForegroundColor Green
                exit 0
            }
        } catch {
            Write-Host "✗ Failed to login. The password might be incorrect." -ForegroundColor Red
            Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
            Write-Host ""
            Write-Host "To fix this, you need to update the password hash in the database." -ForegroundColor Yellow
            Write-Host "See fix_admin_password.sql for instructions." -ForegroundColor Yellow
            exit 1
        }
    } else {
        Write-Host "✗ Failed to register: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
}

# Step 2: Update user role to ADMIN via SQL
Write-Host ""
Write-Host "Step 2: Updating user role to ADMIN..." -ForegroundColor Yellow

$dbHost = "localhost"
$dbPort = "5432"
$dbName = "postgres"
$dbUser = "postgres"
$dbPassword = "12345"

$updateRoleSQL = @"
UPDATE users SET role = 'ADMIN' WHERE email = '$adminEmail';
SELECT id, username, email, role FROM users WHERE email = '$adminEmail';
"@

$env:PGPASSWORD = $dbPassword
Write-Host "Executing SQL update..." -ForegroundColor Cyan
$updateResult = $updateRoleSQL | psql -h $dbHost -p $dbPort -U $dbUser -d $dbName 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ User role updated to ADMIN!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Admin account setup complete!" -ForegroundColor Green
    Write-Host "You can now login with:" -ForegroundColor Cyan
    Write-Host "  Email: $adminEmail" -ForegroundColor White
    Write-Host "  Password: $adminPassword" -ForegroundColor White
} else {
    Write-Host "✗ Failed to update role via SQL." -ForegroundColor Red
    Write-Host "You may need to run this SQL manually:" -ForegroundColor Yellow
    Write-Host "UPDATE users SET role = 'ADMIN' WHERE email = '$adminEmail';" -ForegroundColor White
}

$env:PGPASSWORD = ""
