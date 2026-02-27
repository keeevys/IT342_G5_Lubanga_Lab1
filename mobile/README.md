# DentalBook Mobile App

Android mobile application for DentalBook system built with Kotlin, using RelativeLayout and connecting to MySQL database through REST API.

## Features

- **User Authentication**
  - Login with email or username
  - User registration with validation
  - Secure token-based authentication
  - Persistent login sessions

- **User Dashboard**
  - View user profile information
  - Display user ID, full name, and email
  - Logout functionality

## Architecture

### UI Design
- **Layout Type**: RelativeLayout (as per requirements)
- **Design Language**: Matches web application UX/UI
- **Color Scheme**: Purple gradient theme (#9b59b6, #6c3483)
- **Screens**:
  - Login Activity
  - Register Activity
  - Dashboard Activity

### Data Flow
1. User interacts with UI (RelativeLayout-based activities)
2. Activities communicate with ApiService
3. ApiService reads configuration from XML
4. API calls to backend server
5. Backend connects to MySQL database
6. Response flows back through the layers

## MySQL Database Configuration

The app connects to MySQL Workbench through a REST API backend. Configuration is stored in XML format.

### Configuration File
Location: `app/src/main/res/xml/database_config.xml`

```xml
<database-config>
    <mysql>
        <server>
            <host>localhost</host>
            <port>3306</port>
            <database>dentalbook_db</database>
        </server>
        <credentials>
            <username>root</username>
            <password>password</password>
        </credentials>
    </mysql>
    <api>
        <base-url>http://10.0.2.2:8080/api</base-url>
    </api>
</database-config>
```

### Important Network Notes

**For Android Emulator:**
- Use `10.0.2.2` instead of `localhost` to connect to your computer's localhost
- Example: `http://10.0.2.2:8080/api`

**For Physical Device:**
- Use your computer's IP address on the same network
- Example: `http://192.168.1.100:8080/api`
- Find your IP with `ipconfig` (Windows) or `ifconfig` (Mac/Linux)

## Project Structure

```
mobile/app/src/main/
├── java/com/example/dentalbook/
│   ├── LoginActivity.kt          # Login screen
│   ├── RegisterActivity.kt       # Registration screen
│   ├── DashboardActivity.kt      # Dashboard screen
│   ├── models/
│   │   ├── User.kt               # User data model
│   │   ├── LoginRequest.kt       # Login request model
│   │   ├── RegisterRequest.kt    # Register request model
│   │   ├── AuthResponse.kt       # Authentication response
│   │   └── ApiResponse.kt        # Generic API response
│   ├── services/
│   │   └── ApiService.kt         # REST API communication
│   └── utils/
│       ├── TokenManager.kt       # Token and session management
│       └── DatabaseConfigParser.kt # XML config parser
├── res/
│   ├── layout/
│   │   ├── activity_login.xml    # Login layout (RelativeLayout)
│   │   ├── activity_register.xml # Register layout (RelativeLayout)
│   │   └── activity_dashboard.xml # Dashboard layout (RelativeLayout)
│   ├── drawable/
│   │   ├── gradient_background.xml
│   │   ├── card_background.xml
│   │   ├── input_background.xml
│   │   ├── button_background.xml
│   │   └── info_row_background.xml
│   ├── values/
│   │   ├── colors.xml            # App colors (purple theme)
│   │   └── strings.xml           # String resources
│   └── xml/
│       ├── database_config.xml   # MySQL & API configuration
│       └── network_security_config.xml # Network security
└── AndroidManifest.xml           # App manifest with permissions
```

## Setup Instructions

### Prerequisites
1. Android Studio installed
2. MySQL Workbench with database set up
3. Backend API server running (Spring Boot from `backend/` folder)
4. Android device/emulator with API level 26+

### Step 1: Configure MySQL Database

Ensure MySQL is running with the database:
```sql
CREATE DATABASE dentalbook_db;
USE dentalbook_db;

CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Step 2: Start Backend Server

Navigate to the backend folder and start the Spring Boot server:
```bash
cd backend
mvn spring-boot:run
```

The server should start on `http://localhost:8080`

### Step 3: Configure Mobile App

1. Open `app/src/main/res/xml/database_config.xml`
2. Update the base-url:
   - For emulator: `http://10.0.2.2:8080/api`
   - For physical device: `http://YOUR_IP:8080/api`

### Step 4: Build and Run

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project
4. Run on emulator or physical device

## Key Components

### RelativeLayout Implementation

All screens use RelativeLayout as the primary layout system:

**Login Screen:**
- Centered card with form elements
- Email/Username input field
- Password input field
- Login button
- Link to registration

**Register Screen:**
- Centered card with form elements
- Full name, email, password, confirm password inputs
- Registration button
- Link to login

**Dashboard Screen:**
- Header with title and logout button
- Profile information card
- User details displayed in styled rows

### MySQL Connection via XML

The app uses XML configuration for database connection settings:
- Located in `res/xml/database_config.xml`
- Parsed by `DatabaseConfigParser.kt`
- Used by `ApiService.kt` for API calls
- Backend handles actual MySQL connections

### API Service

The `ApiService` class handles all network communication:
- Login: POST `/api/auth/login`
- Register: POST `/api/auth/register`
- Get Profile: GET `/api/user/profile`

All API calls use coroutines for async operations.

### Token Management

The `TokenManager` class manages authentication:
- Stores JWT tokens in SharedPreferences
- Caches user information locally
- Provides session management
- Handles logout

## Design Matching Web Application

The mobile app faithfully recreates the web application's design:

| Element | Web | Mobile |
|---------|-----|--------|
| Primary Color | #9b59b6 | #9b59b6 |
| Secondary Color | #6c3483 | #6c3483 |
| Background | Linear Gradient | Gradient Drawable |
| Cards | White with border | White with border |
| Buttons | Purple Gradient | Purple Gradient |
| Input Fields | Light purple background | Light purple background |
| Typography | Bold headers | Bold headers |

## Testing

### Test Credentials
After registering a user, you can login with:
- Email or Username: [your registered email]
- Password: [your registered password]

### Network Testing
1. Ensure backend is running
2. Check API endpoint accessibility
3. Verify database connection
4. Test on emulator first
5. Then test on physical device

## Troubleshooting

### Cannot connect to server
- Verify backend is running
- Check IP address in database_config.xml
- Ensure firewall allows connections
- For emulator, use 10.0.2.2 not localhost

### Login fails
- Check MySQL database is running
- Verify user exists in database
- Check backend logs for errors
- Ensure password matches

### Build errors
- Sync Gradle files
- Clean and rebuild project
- Check Android Studio version compatibility
- Verify all dependencies are downloaded

## Dependencies

```kotlin
// Core Android
implementation("androidx.core:core-ktx:1.10.1")
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.11.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
```

## Security Notes

- Passwords are sent securely to backend (ensure HTTPS in production)
- JWT tokens stored in SharedPreferences
- Network security config allows cleartext for development
- Use HTTPS and proper encryption in production

## Future Enhancements

- Add appointment booking features
- Implement image upload for profile
- Add offline mode with local database
- Implement biometric authentication
- Add push notifications

## License

This is a student project for IT342.

## Authors

Group 5 - Lubanga Lab1
