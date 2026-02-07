# Admin Account Information

## Default Admin Account

### Automatic Creation

The admin account is **automatically created** when the backend application starts for the first time.

The `AdminUserInitializer` component runs after the database is initialized and creates the admin user if it doesn't already exist.

### Admin Credentials

- **Email**: `admin@volunteer-system.local`
- **Password**: `admin123`
- **Username**: `Администратор`
- **Role**: `ADMIN`

### How It Works

1. On application startup, `AdminUserInitializer` checks if an admin user exists
2. If not found, it creates a new admin user with:
   - Properly hashed password using BCryptPasswordEncoder
   - ADMIN role
   - Default rating of 5.0
3. The admin user is ready to use immediately after the first startup

### Manual Setup (Alternative)

If you need to create the admin manually, you can use the setup script:
```powershell
.\setup_admin.ps1
```

Or register via API and update role:
```powershell
# Register
$body = @{
    email = "admin@volunteer-system.local"
    password = "admin123"
    fullName = "Администратор"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/v1/auth/register" -Method Post -Body $body -ContentType "application/json"

# Update role to ADMIN via SQL
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@volunteer-system.local';
```

## Important Security Notes

⚠️ **This is a default account for development/testing purposes.**

**For production:**
1. Change the admin password immediately after first login
2. Consider using a stronger password
3. Consider implementing two-factor authentication for admin accounts
4. Review and restrict admin account access as needed

## Accessing Admin Features

Once logged in with the admin account:
- The "Панель админа" (Admin Panel) button will appear in the toolbar
- You can access admin features such as:
  - Managing organization requests
  - Managing organizations
  - Managing user roles
  - Deleting events (if needed)

## Creating Additional Admin Accounts

To create additional admin accounts:

1. Register a new user through the normal registration process
2. Log in as an existing admin
3. Go to Admin Panel → Users
4. Change the user's role to "ADMIN"

Or use SQL directly:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'user@example.com';
```

## Resetting Admin Password

If you need to reset the admin password, you can:

1. Use the password reset functionality (if implemented)
2. Or update directly in the database:
   ```sql
   -- Generate a new BCrypt hash for your new password
   -- Then update:
   UPDATE users 
   SET password_hash_bcrypt = '$2a$10$YOUR_NEW_HASH_HERE' 
   WHERE email = 'admin@volunteer-system.local';
   ```

## Generating BCrypt Hashes

To generate a BCrypt hash for a password, you can:

1. Use the Java utility: `com.volunteer.util.PasswordHashGenerator`
2. Use an online BCrypt generator (for development only)
3. Use Spring's BCryptPasswordEncoder in a test/utility class
