Here's a comprehensive documentation for your Photography App:

```markdown:README.md
# Photography App

A Spring Boot-based RESTful API for managing photos and albums. This application provides functionality for user authentication, photo management, album organization, and social features.

## Table of Contents
1. [Features](#features)
2. [Technology Stack](#technology-stack)
3. [Getting Started](#getting-started)
4. [API Documentation](#api-documentation)
5. [Database Schema](#database-schema)
6. [Security](#security)
7. [File Storage](#file-storage)
8. [Error Handling](#error-handling)

## Features
- User authentication and authorization using JWT
- Photo upload and management
- Album organization
- Profile management with profile pictures
- Tag-based photo search
- Secure file storage

## Technology Stack
- Java 17
- Spring Boot 3.x
- Spring Security
- JWT Authentication
- MySQL/PostgreSQL
- Maven
- Spring Data JPA

## Getting Started

### Prerequisites
- JDK 17 or higher
- Maven 3.x
- MySQL 8.x or PostgreSQL
- Git

### Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/photography-app.git
cd photography-app
```

2. Configure application properties:
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/photography_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
jwt.secret=your_jwt_secret
jwt.expiration=86400000

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

3. Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

## API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "user123",
    "password": "password123",
    "name": "John Doe",
    "email": "john@example.com",
    "bio": "Photography enthusiast"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "user123",
    "password": "password123"
}
```

### User Endpoints

#### Get Current User Profile
```http
GET /api/users/me
Authorization: Bearer {jwt_token}
```

#### Update Profile
```http
PUT /api/users/me
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "bio": "Updated bio"
}
```

#### Update Profile Image
```http
PUT /api/users/me/profile-image
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

file: [image_file]
```

### Album Endpoints

#### Create Album
```http
POST /api/albums
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "title": "Summer Vacation 2024",
    "description": "Photos from our trip to Hawaii"
}
```

#### Get User Albums
```http
GET /api/albums/me
Authorization: Bearer {jwt_token}
```

#### Get Specific Album
```http
GET /api/albums/{albumId}
Authorization: Bearer {jwt_token}
```

### Photo Endpoints

#### Upload Photo
```http
POST /api/photos/{albumId}
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

file: [image_file]
request: {
    "title": "Sunset at Beach",
    "description": "Beautiful sunset at Waikiki",
    "tags": ["sunset", "beach", "hawaii"]
}
```

#### Get Album Photos
```http
GET /api/photos/album/{albumId}
Authorization: Bearer {jwt_token}
```

#### Update Photo
```http
PUT /api/photos/{photoId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "title": "Updated Title",
    "description": "Updated description",
    "tags": ["updated", "tags"]
}
```

#### Search Photos by Tags
```http
GET /api/photos/search?tags=sunset,beach
Authorization: Bearer {jwt_token}
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    bio TEXT
);
```

### Albums Table
```sql
CREATE TABLE albums (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Photos Table
```sql
CREATE TABLE photos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255),
    description TEXT,
    image_url VARCHAR(255) NOT NULL,
    album_id BIGINT,
    FOREIGN KEY (album_id) REFERENCES albums(id)
);
```

### Photo Tags Table
```sql
CREATE TABLE photo_tags (
    photo_id BIGINT,
    tag VARCHAR(255),
    PRIMARY KEY (photo_id, tag),
    FOREIGN KEY (photo_id) REFERENCES photos(id)
);
```

## Security
- JWT-based authentication
- Password encryption using BCrypt
- Role-based authorization
- Secure file upload handling

## File Storage
- Photos are stored in the `uploads/photos` directory
- Profile images are stored in `uploads/profile-images`
- Unique filenames are generated using UUID
- Original filenames are preserved as a suffix

## Error Handling

### Common Error Responses
```json
{
    "error": "Error message",
    "status": 400,
    "timestamp": "2024-03-20T10:30:00Z"
}
```

### HTTP Status Codes
- 200: Success
- 201: Created
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

