# Script to generate BCrypt hash for admin password using Gradle

Write-Host "Generating BCrypt hash for admin password..." -ForegroundColor Yellow
Write-Host ""

cd backend

# Compile the utility
Write-Host "Compiling..." -ForegroundColor Cyan
.\gradlew.bat compileJava --no-daemon 2>&1 | Out-Null

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

# Find the spring-security-crypto jar
$gradleCache = "$env:USERPROFILE\.gradle\caches\modules-2\files-2.1"
$springSecurityPath = Get-ChildItem -Path $gradleCache -Recurse -Filter "spring-security-crypto*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1

if (-not $springSecurityPath) {
    Write-Host "Could not find spring-security-crypto jar. Trying alternative approach..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Please run this manually:" -ForegroundColor Cyan
    Write-Host "1. Start the backend application" -ForegroundColor White
    Write-Host "2. Register a new user via API with email 'admin@volunteer-system.local' and password 'admin123'" -ForegroundColor White
    Write-Host "3. Then update the role to ADMIN using SQL:" -ForegroundColor White
    Write-Host "   UPDATE users SET role = 'ADMIN' WHERE email = 'admin@volunteer-system.local';" -ForegroundColor White
    exit 1
}

$classpath = "build\classes\java\main;$($springSecurityPath.FullName)"

Write-Host "Running hash generator..." -ForegroundColor Cyan
java -cp $classpath com.volunteer.util.GenerateAdminPassword 2>&1

cd ..
