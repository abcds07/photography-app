package com.example.photography.service;

import com.example.photography.dto.PhotoRequest;
import com.example.photography.dto.PhotoResponse;
import com.example.photography.model.Album;
import com.example.photography.model.Photo;
import com.example.photography.model.User;
import com.example.photography.repository.AlbumRepository;
import com.example.photography.repository.PhotoRepository;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;
    
    @Autowired
    private AlbumRepository albumRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private static final String UPLOAD_DIR = "uploads/photos/";

    public PhotoResponse uploadPhoto(Long albumId, MultipartFile file, PhotoRequest request) throws IOException {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        
        User currentUser = getCurrentUser();
        if (!album.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to upload to this album");
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        
        Files.copy(file.getInputStream(), filePath);

        Photo photo = new Photo();
        photo.setTitle(request.getTitle());
        photo.setDescription(request.getDescription());
        photo.setImageUrl(UPLOAD_DIR + filename);
        photo.setAlbum(album);
        photo.setTags(request.getTags());

        Photo savedPhoto = photoRepository.save(photo);

        PhotoResponse response = new PhotoResponse();
        response.setId(savedPhoto.getId());
        response.setTitle(savedPhoto.getTitle());
        response.setDescription(savedPhoto.getDescription());
        response.setImageUrl(savedPhoto.getImageUrl());
        response.setAlbumId(album.getId());
        response.setAlbumTitle(album.getTitle());
        response.setUserId(currentUser.getId());
        response.setUsername(currentUser.getUsername());
        response.setTags(savedPhoto.getTags());

        return response;
    }

    public List<PhotoResponse> getAlbumPhotos(Long albumId) {
        return photoRepository.findByAlbumId(albumId)
                .stream()
                .map(this::createPhotoResponse)
                .collect(Collectors.toList());
    }

    public PhotoResponse getPhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        return createPhotoResponse(photo);
    }

    public PhotoResponse updatePhoto(Long photoId, PhotoRequest request) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        User currentUser = getCurrentUser();
        if (!photo.getAlbum().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to update this photo");
        }

        photo.setTitle(request.getTitle());
        photo.setDescription(request.getDescription());
        photo.setTags(request.getTags());

        return createPhotoResponse(photoRepository.save(photo));
    }

    public void deletePhoto(Long photoId) throws IOException {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        User currentUser = getCurrentUser();
        if (!photo.getAlbum().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to delete this photo");
        }

        Files.deleteIfExists(Paths.get(photo.getImageUrl()));
        photoRepository.delete(photo);
    }

    public List<PhotoResponse> searchPhotosByTags(Set<String> tags) {
        return photoRepository.findByTags(tags)
                .stream()
                .map(this::createPhotoResponse)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private PhotoResponse createPhotoResponse(Photo photo) {
        PhotoResponse response = new PhotoResponse();
        response.setId(photo.getId());
        response.setTitle(photo.getTitle());
        response.setDescription(photo.getDescription());
        response.setImageUrl(photo.getImageUrl());
        response.setAlbumId(photo.getAlbum().getId());
        response.setAlbumTitle(photo.getAlbum().getTitle());
        response.setUserId(photo.getAlbum().getUser().getId());
        response.setUsername(photo.getAlbum().getUser().getUsername());
        response.setTags(photo.getTags());
        return response;
    }
}