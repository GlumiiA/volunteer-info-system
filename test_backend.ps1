# Backend API Testing Script
$baseUrl = "http://localhost:8080/api/v1"

Write-Host "`n=== Backend API Testing ===`n" -ForegroundColor Cyan

# Test 1: Public Organizations Endpoint
Write-Host "1. Testing GET /organizations (public)..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/organizations" -Method GET -UseBasicParsing -TimeoutSec 5
    Write-Host "   ✓ Status: $($response.StatusCode) OK" -ForegroundColor Green
    $content = $response.Content | ConvertFrom-Json
    Write-Host "   Response: $($content | ConvertTo-Json -Compress)"
} catch {
    Write-Host "   ✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Public Leaderboard Endpoint
Write-Host "`n2. Testing GET /leaderboard (public)..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/leaderboard?limit=10" -Method GET -UseBasicParsing -TimeoutSec 5
    Write-Host "   ✓ Status: $($response.StatusCode) OK" -ForegroundColor Green
    $content = $response.Content | ConvertFrom-Json
    Write-Host "   Leaderboard entries: $($content.leaderboard.Count)"
} catch {
    Write-Host "   ✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Public Mass Events Endpoint
Write-Host "`n3. Testing GET /events/mass (public)..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/events/mass" -Method GET -UseBasicParsing -TimeoutSec 5
    Write-Host "   ✓ Status: $($response.StatusCode) OK" -ForegroundColor Green
    $content = $response.Content | ConvertFrom-Json
    Write-Host "   Mass events: $($content.Count)"
} catch {
    Write-Host "   ✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Public Individual Events Endpoint
Write-Host "`n4. Testing GET /events/individual (public)..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/events/individual" -Method GET -UseBasicParsing -TimeoutSec 5
    Write-Host "   ✓ Status: $($response.StatusCode) OK" -ForegroundColor Green
    $content = $response.Content | ConvertFrom-Json
    Write-Host "   Individual events: $($content.Count)"
} catch {
    Write-Host "   ✗ Failed: Status $($_.Exception.Response.StatusCode.value__) - $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "   Error body: $responseBody" -ForegroundColor Red
    }
}

# Test 5: Registration Endpoint
Write-Host "`n5. Testing POST /auth/register..." -ForegroundColor Yellow
try {
    $body = @{
        email = "testuser_$(Get-Random)@example.com"
        password = "TestPassword123!"
        fullName = "Test User"
    } | ConvertTo-Json
    $headers = @{'Content-Type'='application/json'}
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/register" -Method POST -Headers $headers -Body $body -UseBasicParsing -TimeoutSec 5
    Write-Host "   ✓ Status: $($response.StatusCode) OK" -ForegroundColor Green
    $content = $response.Content | ConvertFrom-Json
    Write-Host "   User created: $($content.user.name) (ID: $($content.user.id))"
} catch {
    Write-Host "   ✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Swagger/OpenAPI Docs
Write-Host "`n6. Testing Swagger UI..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/../v3/api-docs" -Method GET -UseBasicParsing -TimeoutSec 5
    Write-Host "   ✓ OpenAPI docs accessible: Status $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "   ✗ Failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Testing Complete ===`n" -ForegroundColor Cyan
