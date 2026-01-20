# PowerShell script to populate test data via API endpoints
$baseUrl = "http://localhost:8080/api/v1"
$headers = @{'Content-Type'='application/json'}

Write-Host "=== Populating Test Data via API ===`n" -ForegroundColor Cyan

# Helper function to make API calls
function Invoke-ApiRequest {
    param(
        [string]$Method,
        [string]$Uri,
        [hashtable]$Headers,
        [string]$Body = $null,
        [string]$Token = $null
    )
    
    if ($Token) {
        $Headers['Authorization'] = "Bearer $Token"
    }
    
    try {
        $params = @{
            Uri = $Uri
            Method = $Method
            Headers = $Headers
            UseBasicParsing = $true
            TimeoutSec = 10
        }
        
        if ($Body) {
            $params['Body'] = $Body
        }
        
        $response = Invoke-WebRequest @params
        return @{
            Success = $true
            StatusCode = $response.StatusCode
            Content = $response.Content | ConvertFrom-Json
        }
    } catch {
        $statusCode = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "Error" }
        return @{
            Success = $false
            StatusCode = $statusCode
            Message = $_.Exception.Message
        }
    }
}

# 1. Register test users
Write-Host "1. Registering test users..." -ForegroundColor Yellow

$users = @(
    @{email="admin@test.com"; password="password123"; fullName="Admin User"; role="admin"},
    @{email="org1@test.com"; password="password123"; fullName="Organization Rep 1"},
    @{email="org2@test.com"; password="password123"; fullName="Organization Rep 2"},
    @{email="user1@test.com"; password="password123"; fullName="Test User 1"},
    @{email="user2@test.com"; password="password123"; fullName="Test User 2"}
)

$tokens = @{}

foreach ($user in $users) {
    $body = @{
        email = $user.email
        password = $user.password
        fullName = $user.fullName
    } | ConvertTo-Json
    
    $result = Invoke-ApiRequest -Method "POST" -Uri "$baseUrl/auth/register" -Headers $headers -Body $body
    
    if ($result.Success) {
        $tokens[$user.email] = $result.Content.token
        Write-Host "   ✓ Registered: $($user.email) (Token: $($result.Content.token.Substring(0, 20))...)" -ForegroundColor Green
    } else {
        if ($result.StatusCode -eq 400 -and $result.Message -like "*already exists*") {
            # Try to login instead
            $loginBody = @{
                email = $user.email
                password = $user.password
            } | ConvertTo-Json
            
            $loginResult = Invoke-ApiRequest -Method "POST" -Uri "$baseUrl/auth/login" -Headers $headers -Body $loginBody
            if ($loginResult.Success) {
                $tokens[$user.email] = $loginResult.Content.token
                Write-Host "   ✓ Logged in: $($user.email) (already exists)" -ForegroundColor Green
            } else {
                Write-Host "   ✗ Failed: $($user.email) - $($loginResult.Message)" -ForegroundColor Red
            }
        } else {
            Write-Host "   ✗ Failed: $($user.email) - $($result.Message)" -ForegroundColor Red
        }
    }
}

# 2. Get organizations (should be populated or create them manually)
Write-Host "`n2. Checking organizations..." -ForegroundColor Yellow
$orgResult = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/organizations" -Headers $headers
if ($orgResult.Success) {
    Write-Host "   ✓ Organizations found: $($orgResult.Content.Count)" -ForegroundColor Green
    $orgResult.Content | ForEach-Object { Write-Host "      - $($_.name) (ID: $($_.id))" }
} else {
    Write-Host "   ⚠ Organizations endpoint returned error" -ForegroundColor Yellow
}

# 3. Create mass events (need org token)
Write-Host "`n3. Creating mass events..." -ForegroundColor Yellow
if ($tokens.ContainsKey("org1@test.com")) {
    $token = $tokens["org1@test.com"]
    $eventBody = @{
        title = "Помощь детскому дому `"Солнышко`""
        description = "Организация праздника для детей, ремонт и благоустройство территории"
        dateStart = (Get-Date).AddDays(5).ToString("yyyy-MM-ddTHH:mm:ssZ")
        dateEnd = (Get-Date).AddDays(5).AddHours(8).ToString("yyyy-MM-ddTHH:mm:ssZ")
        workHours = 8.0
        address = "г. Санкт-Петербург, ул. Детская, д. 10"
        volunteersRequired = 15
        ageRestriction = 18
    } | ConvertTo-Json -Depth 10
    
    $eventResult = Invoke-ApiRequest -Method "POST" -Uri "$baseUrl/events/mass" -Headers $headers -Body $eventBody -Token $token
    if ($eventResult.Success) {
        Write-Host "   ✓ Created mass event: $($eventResult.Content.title)" -ForegroundColor Green
    } else {
        Write-Host "   ✗ Failed to create mass event: $($eventResult.Message)" -ForegroundColor Red
    }
} else {
    Write-Host "   ⚠ Skipping - no organization user token available" -ForegroundColor Yellow
}

# 4. Get events
Write-Host "`n4. Fetching events..." -ForegroundColor Yellow
$massEventsResult = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/events/mass" -Headers $headers
if ($massEventsResult.Success) {
    Write-Host "   ✓ Mass events: $($massEventsResult.Content.Count)" -ForegroundColor Green
    $massEventsResult.Content | ForEach-Object { Write-Host "      - $($_.title) (ID: $($_.id))" }
}

$individualEventsResult = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/events/individual" -Headers $headers
if ($individualEventsResult.Success) {
    Write-Host "   ✓ Individual events: $($individualEventsResult.Content.Count)" -ForegroundColor Green
    $individualEventsResult.Content | ForEach-Object { Write-Host "      - $($_.title) (ID: $($_.id))" }
}

# 5. Test leaderboard
Write-Host "`n5. Testing leaderboard..." -ForegroundColor Yellow
$leaderboardResult = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/leaderboard?limit=10" -Headers $headers
if ($leaderboardResult.Success) {
    Write-Host "   ✓ Leaderboard entries: $($leaderboardResult.Content.leaderboard.Count)" -ForegroundColor Green
    $leaderboardResult.Content.leaderboard | Select-Object -First 5 | ForEach-Object {
        Write-Host "      $($_.place). $($_.fullName) - $($_.volunteerHours) hours"
    }
}

# 6. Test authenticated endpoint (GET /users/me)
Write-Host "`n6. Testing authenticated endpoints..." -ForegroundColor Yellow
if ($tokens.ContainsKey("user1@test.com")) {
    $token = $tokens["user1@test.com"]
    $meResult = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/users/me" -Headers $headers -Token $token
    if ($meResult.Success) {
        Write-Host "   ✓ GET /users/me: $($meResult.Content.name) ($($meResult.Content.email))" -ForegroundColor Green
    } else {
        Write-Host "   ✗ GET /users/me failed: $($meResult.Message)" -ForegroundColor Red
    }
}

Write-Host "`n=== Test Data Population Complete ===`n" -ForegroundColor Cyan
