# Admin Account Information

## Default Admin Account

### Quick Setup (Recommended)

Run the setup script:
```powershell
.\setup_admin.ps1
```

This will:
1. Register the admin user via API (or login if exists)
2. Update the user's role to ADMIN
3. Verify the account is ready

### Manual Setup

If the script doesn't work, you can set up manually:

1. **Register the user via API:**
   ```powershell
   # Make sure backend is running
   $body = @{
       email = "admin@volunteer-system.local"
       password = "admin123"
       fullName = "Администратор"
   } | ConvertTo-Json
   
   Invoke-RestMethod -Uri "http://localhost:8080/api/v1/auth/register" -Method Post -Body $body -ContentType "application/json"
   ```

2. **Update role to ADMIN via SQL:**
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE email = 'admin@volunteer-system.local';
   ```

### Admin Credentials

- **Email**: `admin@volunteer-system.local`
- **Password**: `admin123`
- **Username**: `Администратор`
- **Role**: `ADMIN`

**Note**: The admin user is created via the setup script, which ensures the password hash is generated correctly by the backend API. This is more reliable than using a pre-generated hash in the database.

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
