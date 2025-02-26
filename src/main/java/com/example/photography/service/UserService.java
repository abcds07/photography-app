package com.example.photography.service;

import com.example.photography.dto.AlbumResponse;
import com.example.photography.dto.UserRequest;
import com.example.photography.dto.UserResponse;
import com.example.photography.model.Photo;
import com.example.photography.model.User;
import com.example.photography.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/profile-images/";

    public UserResponse getCurrentUserProfile() {
        return createUserResponse(getCurrentUser());
    }

    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return createUserResponse(user);
    }

    public UserResponse updateProfile(UserRequest request) {
        User currentUser = getCurrentUser();

        if (request.getEmail() != null && !request.getEmail().equals(currentUser.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        currentUser.setName(request.getName());
        currentUser.setEmail(request.getEmail());
        currentUser.setBio(request.getBio());

        return createUserResponse(userRepository.save(currentUser));
    }

    public UserResponse updateProfileImage(MultipartFile file) throws IOException {
        User currentUser = getCurrentUser();

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        
        Files.copy(file.getInputStream(), filePath);

        if (currentUser.getProfileImage() != null) {
            Files.deleteIfExists(Paths.get(currentUser.getProfileImage().getImageUrl()));
        } else {
            currentUser.setProfileImage(new Photo());
        }

        currentUser.getProfileImage().setImageUrl(UPLOAD_DIR + filename);
        return createUserResponse(userRepository.save(currentUser));
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setBio(user.getBio());
        if (user.getProfileImage() != null) {
            response.setProfileImageUrl(user.getProfileImage().getImageUrl());
        }

        List<AlbumResponse> albumResponses = user.getAlbums().stream()
                .map(album -> {
                    AlbumResponse albumResponse = new AlbumResponse();
                    albumResponse.setId(album.getId());
                    albumResponse.setTitle(album.getTitle());
                    albumResponse.setDescription(album.getDescription());
                    albumResponse.setUserId(user.getId());
                    albumResponse.setUsername(user.getUsername());
                    return albumResponse;
                })
                .collect(Collectors.toList());

        response.setAlbums(albumResponses);
        return response;
    }
} 