# Authentication System - Lab 1

This project implements a full-stack authentication system with Spring Boot backend and React frontend.

## 📋 Features Implemented

### Backend (Spring Boot)
✅ **User Entity** with attributes:
- userId (Integer, auto-generated)
- username (String, unique)
- email (String, unique)
- password (String, BCrypt encrypted)
- role (String)
- createdAt, updatedAt (LocalDateTime)

✅ **UserRepository** with methods:
- saveUser()
- findByEmail()
- findByUsername()
- existsByEmail()
- existsByUsername()

✅ **Authentication Endpoints**:
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token
- `POST /api/auth/logout` - Logout (invalidate session)

✅ **Protected Endpoint**:
- `GET /api/user/me` - Get current user profile (requires JWT token)

✅ **Security Features**:
- BCrypt password encryption
- JWT token-based authentication
- Spring Security configuration
- CORS enabled for frontend integration

### Frontend (React)
✅ **Pages**:
- Register page (`/register`)
- Login page (`/login`)
- Dashboard/Profile page (`/dashboard`) - Protected route

✅ **Features**:
- Form validation
- Error handling
- Token storage in localStorage
- Automatic redirection for protected routes
- Logout functionality

## 🚀 Setup Instructions

### Prerequisites
1. Java 17 or higher
2. MySQL Server
3. Node.js and npm
4. Maven

### Backend Setup

1. **Configure MySQL Database**:
   - Open MySQL and create a database or let the app create it automatically
   - Update credentials in `backend/src/main/resources/application.properties` if needed:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/auth_db?createDatabaseIfNotExist=true
     spring.datasource.username=root
     spring.datasource.password=root
     ```

2. **Run the Backend**:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```bash
   cd backend
   mvnw.cmd spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Install Dependencies**:
   ```bash
   cd web
   npm install
   ```

2. **Start the React App**:
   ```bash
   npm start
   ```

   The frontend will start on `http://localhost:3000`

## 📝 API Endpoints

### Public Endpoints

**Register User**
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Login**
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "emailOrUsername": "john@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "USER"
}
```

### Protected Endpoints

**Get Current User Profile**
```http
GET http://localhost:8080/api/user/me
Authorization: Bearer <your-jwt-token>
```

Response:
```json
{
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "USER",
  "createdAt": "2026-02-13T10:30:00"
}
```

## 🏗️ Project Structure

### Backend Structure
```
backend/
├── src/main/java/backend/g5/
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   └── UserController.java
│   ├── dto/
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── AuthResponse.java
│   │   ├── UserResponse.java
│   │   └── MessageResponse.java
│   ├── entity/
│   │   └── User.java
│   ├── repository/
│   │   └── UserRepository.java
│   ├── security/
│   │   └── JwtAuthenticationFilter.java
│   ├── service/
│   │   ├── AuthService.java
│   │   └── CustomUserDetailsService.java
│   ├── util/
│   │   └── JwtUtils.java
│   └── G5Application.java
└── src/main/resources/
    └── application.properties
```

### Frontend Structure
```
web/
├── src/
│   ├── components/
│   │   ├── Register.js
│   │   ├── Login.js
│   │   └── Dashboard.js
│   ├── services/
│   │   ├── authService.js
│   │   └── userService.js
│   ├── styles/
│   │   ├── Auth.css
│   │   └── Dashboard.css
│   └── App.js
└── package.json
```

## 🔐 Security Features

1. **Password Encryption**: All passwords are encrypted using BCrypt before storing in the database
2. **JWT Authentication**: Stateless authentication using JWT tokens
3. **Token Expiration**: Tokens expire after 24 hours (configurable)
4. **Protected Routes**: Frontend automatically redirects unauthenticated users to login
5. **CORS Configuration**: Properly configured for cross-origin requests

## 📚 Technologies Used

### Backend
- Spring Boot 4.0.2
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Tokens)
- BCrypt
- Maven

### Frontend
- React 19.2.4
- React Router DOM 6.21.3
- Axios 1.6.5
- CSS3

## 🎯 Class Diagram Implementation

The implementation follows the provided class diagram:
- **User Entity**: userId, username, email, password, role
- **AuthController**: registerUser(), loginUser(), logoutUser()
- **AuthService**: register(), authenticate(), invalidateSession()
- **UserRepository**: saveUser(), findByEmail(), findByUsername()

## 🧪 Testing

### Manual Testing Steps

1. **Register a new user**:
   - Go to `http://localhost:3000/register`
   - Fill in the registration form
   - Submit

2. **Login**:
   - Go to `http://localhost:3000/login`
   - Enter your credentials
   - Submit

3. **Access Dashboard**:
   - After successful login, you'll be redirected to the dashboard
   - View your profile information

4. **Logout**:
   - Click the "Logout" button on the dashboard
   - You'll be redirected to the login page

## ⚠️ Notes

- Make sure MySQL is running before starting the backend
- The backend must be running for the frontend to work
- Default MySQL credentials are `root:root` - change if needed
- JWT secret key is configured in application.properties
- The database schema is automatically created by Hibernate

## 🎓 Lab Requirements Fulfilled

✅ Backend – Spring Boot
- ✅ POST /api/auth/register
- ✅ POST /api/auth/login
- ✅ GET /api/user/me (protected)
- ✅ Database connection (MySQL)
- ✅ Password encryption (BCrypt)

✅ Web Application – ReactJS
- ✅ Register page
- ✅ Login page
- ✅ Dashboard/Profile page (protected)
- ✅ Logout functionality
