# Script to create admin user via API
# This will register an admin user and then update their role to ADMIN

$apiUrl = "http://localhost:8080/api/v1"
$adminEmail = "admin@volunteer-system.local"
$adminPassword = "admin123"
$adminName = "Администратор"

Write-Host "Creating admin user..." -ForegroundColor Yellow
Write-Host "Email: $adminEmail" -ForegroundColor Cyan
Write-Host "Password: $adminPassword" -ForegroundColor Cyan
Write-Host ""

# Step 1: Register the user
Write-Host "Step 1: Registering user..." -ForegroundColor Yellow
$registerBody = @{
    email = $adminEmail
    password = $adminPassword
    fullName = $adminName
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$apiUrl/auth/register" -Method Post -Body $registerBody -ContentType "application/json"
    $token = $registerResponse.token
    Write-Host "User registered successfully!" -ForegroundColor Green
    Write-Host "Token: $($token.Substring(0, 20))..." -ForegroundColor Gray
} catch {
    if ($_.Exception.Response.StatusCode -eq 400) {
        Write-Host "User already exists. Attempting to login..." -ForegroundColor Yellow
        # Try to login instead
        $loginBody = @{
            email = $adminEmail
            password = $adminPassword
        } | ConvertTo-Json
        
        try {
            $loginResponse = Invoke-RestMethod -Uri "$apiUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
            $token = $loginResponse.token
            $userId = $loginResponse.user.id
            Write-Host "Logged in successfully!" -ForegroundColor Green
            
            # Check if already admin
            if ($loginResponse.user.role -eq "ADMIN") {
                Write-Host "User is already an admin!" -ForegroundColor Green
                exit 0
            }
        } catch {
            Write-Host "Failed to login: $($_.Exception.Message)" -ForegroundColor Red
            exit 1
        }
    } else {
        Write-Host "Failed to register: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
}

# Step 2: Update user role to ADMIN (requires direct database access or admin endpoint)
Write-Host ""
Write-Host "Step 2: Updating user role to ADMIN..." -ForegroundColor Yellow
Write-Host "NOTE: This requires direct database access or an existing admin account." -ForegroundColor Yellow
Write-Host ""
Write-Host "To complete admin setup, run this SQL:" -ForegroundColor Cyan
Write-Host "UPDATE users SET role = 'ADMIN' WHERE email = '$adminEmail';" -ForegroundColor White
Write-Host ""
Write-Host "Or use the admin panel if you have another admin account." -ForegroundColor Yellow
