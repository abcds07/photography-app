# Photography App

A Spring Boot application for managing photos and albums.

## Features
- User authentication and authorization
- Photo upload and management
- Album organization
- Tag-based photo search
- Profile management

## Setup
1. Clone the repository
2. Copy `application.properties.example` to `application.properties`
3. Update the properties with your database and JWT configuration
4. Run the application using Maven: `mvn spring:run`

## API Documentation
### Authentication
- POST /api/auth/register - Register new user
- POST /api/auth/login - Login user

### Users
- GET /api/users/me - Get current user profile
- PUT /api/users/me - Update profile
- PUT /api/users/me/profile-image - Update profile image

### Albums
- POST /api/albums - Create new album
- GET /api/albums/me - Get current user's albums
- GET /api/albums/{albumId} - Get specific album

### Photos
- POST /api/photos/{albumId} - Upload photo to album
- GET /api/photos/album/{albumId} - Get album photos
- PUT /api/photos/{photoId} - Update photo details
- DELETE /api/photos/{photoId} - Delete photo

## Technologies
- Spring Boot
- Spring Security
- JWT Authentication
- MySQL/PostgreSQL
- Maven
