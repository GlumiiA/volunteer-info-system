# Comprehensive API Testing Script
$baseUrl = "http://localhost:8080/api/v1"
$headers = @{'Content-Type'='application/json'}

Write-Host "`n=== API Endpoint Testing ===`n" -ForegroundColor Cyan

# Helper function
function Test-Endpoint {
    param($Method, $Uri, $Headers, $Body = $null, $Token = $null, $ExpectedStatus = 200)
    
    if ($Token) {
        $Headers = $Headers.Clone()
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
        if ($Body) { $params['Body'] = $Body }
        
        $response = Invoke-WebRequest @params
        $statusOk = ($response.StatusCode -eq $ExpectedStatus)
        $statusColor = if ($statusOk) { "Green" } else { "Yellow" }
        
        Write-Host "  [$($response.StatusCode)] $Method $Uri" -ForegroundColor $statusColor
        
        if ($response.Content) {
            try {
                $data = $response.Content | ConvertFrom-Json
                if ($data.Count -ne $null) {
                    Write-Host "    -> Found $($data.Count) items" -ForegroundColor Gray
                } elseif ($data.leaderboard) {
                    Write-Host "    -> Leaderboard entries: $($data.leaderboard.Count)" -ForegroundColor Gray
                }
            } catch { }
        }
        
        return @{Success = $true; StatusCode = $response.StatusCode; Content = $response.Content; Token = $null}
    } catch {
        $status = if ($_.Exception.Response) { $_.Exception.Response.StatusCode.value__ } else { "Error" }
        $color = if ($status -eq 401 -or $status -eq 403) { "Yellow" } else { "Red" }
        Write-Host "  [$status] $Method $Uri - $($_.Exception.Message)" -ForegroundColor $color
        return @{Success = $false; StatusCode = $status; Error = $_.Exception.Message; Token = $null}
    }
}

# 1. Test Public Endpoints
Write-Host "1. Public Endpoints:" -ForegroundColor Yellow
Test-Endpoint "GET" "$baseUrl/organizations" $headers | Out-Null
Test-Endpoint "GET" "$baseUrl/events/mass" $headers | Out-Null
Test-Endpoint "GET" "$baseUrl/events/individual" $headers | Out-Null
Test-Endpoint "GET" "$baseUrl/leaderboard?limit=10" $headers | Out-Null

# 2. Register and Login Users
Write-Host "`n2. Authentication:" -ForegroundColor Yellow

# Register admin user
$registerBody = '{"email":"admin@test.com","password":"password123","fullName":"Admin User"}'
$result = Test-Endpoint "POST" "$baseUrl/auth/register" $headers $registerBody
if (-not $result.Success) {
    # Try login instead
    $loginBody = '{"email":"admin@test.com","password":"password123"}'
    $result = Test-Endpoint "POST" "$baseUrl/auth/login" $headers $loginBody
}
$adminToken = if ($result.Success) { 
    ($result.Content | ConvertFrom-Json).token 
} else { $null }

# Register org rep
$registerBody = '{"email":"org1@test.com","password":"password123","fullName":"Org Rep 1"}'
$result = Test-Endpoint "POST" "$baseUrl/auth/register" $headers $registerBody
if (-not $result.Success) {
    $loginBody = '{"email":"org1@test.com","password":"password123"}'
    $result = Test-Endpoint "POST" "$baseUrl/auth/login" $headers $loginBody
}
$orgToken = if ($result.Success) { 
    ($result.Content | ConvertFrom-Json).token 
} else { $null }

# Register regular user
$registerBody = '{"email":"user1@test.com","password":"password123","fullName":"Test User 1"}'
$result = Test-Endpoint "POST" "$baseUrl/auth/register" $headers $registerBody
if (-not $result.Success) {
    $loginBody = '{"email":"user1@test.com","password":"password123"}'
    $result = Test-Endpoint "POST" "$baseUrl/auth/login" $headers $loginBody
}
$userToken = if ($result.Success) { 
    ($result.Content | ConvertFrom-Json).token 
} else { $null }

Write-Host "  Admin token: $(if($adminToken){'OK'}else{'Failed'})" -ForegroundColor $(if($adminToken){"Green"}else{"Red"})
Write-Host "  Org token: $(if($orgToken){'OK'}else{'Failed'})" -ForegroundColor $(if($orgToken){"Green"}else{"Red"})
Write-Host "  User token: $(if($userToken){'OK'}else{'Failed'})" -ForegroundColor $(if($userToken){"Green"}else{"Red"})

# 3. Test Authenticated Endpoints
Write-Host "`n3. Authenticated Endpoints:" -ForegroundColor Yellow
if ($userToken) {
    Test-Endpoint "GET" "$baseUrl/users/me" $headers -Token $userToken | Out-Null
}

# 4. Create Organizations (admin only)
Write-Host "`n4. Creating Organizations (Admin):" -ForegroundColor Yellow
if ($adminToken) {
    $orgBody = '{"name":"Test Organization 1","description":"Test org description","address":"Test Address 1"}'
    $result = Test-Endpoint "POST" "$baseUrl/admin/organizations" $headers $orgBody -Token $adminToken
    if ($result.Success) {
        Write-Host "  Organization created successfully" -ForegroundColor Green
    } else {
        Write-Host "  Failed to create organization (might need ADMIN role)" -ForegroundColor Yellow
    }
}

# 5. Test Mass Events Again
Write-Host "`n5. Re-testing Mass Events:" -ForegroundColor Yellow
Test-Endpoint "GET" "$baseUrl/events/mass" $headers | Out-Null

Write-Host "`n=== Testing Complete ===`n" -ForegroundColor Cyan
