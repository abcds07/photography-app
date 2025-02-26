package com.example.photography.service;

import com.example.photography.dto.AlbumRequest;
import com.example.photography.dto.AlbumResponse;
import com.example.photography.dto.PhotoResponse;
import com.example.photography.model.Album;
import com.example.photography.model.User;
import com.example.photography.repository.AlbumRepository;
import com.example.photography.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AlbumResponse> getCurrentUserAlbums() {
        User currentUser = getCurrentUser();
        return albumRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::createAlbumResponse)
                .collect(Collectors.toList());
    }

    public AlbumResponse createAlbum(AlbumRequest request) {
        User currentUser = getCurrentUser();

        Album album = new Album();
        album.setTitle(request.getTitle());
        album.setDescription(request.getDescription());
        album.setUser(currentUser);

        return createAlbumResponse(albumRepository.save(album));
    }

    public List<AlbumResponse> getUserAlbums(Long userId) {
        return albumRepository.findByUserId(userId)
                .stream()
                .map(this::createAlbumResponse)
                .collect(Collectors.toList());
    }

    public AlbumResponse getAlbum(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        return createAlbumResponse(album);
    }

    public AlbumResponse updateAlbum(Long albumId, AlbumRequest request) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        User currentUser = getCurrentUser();
        if (!album.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to update this album");
        }

        album.setTitle(request.getTitle());
        album.setDescription(request.getDescription());

        return createAlbumResponse(albumRepository.save(album));
    }

    public void deleteAlbum(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        User currentUser = getCurrentUser();
        if (!album.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to delete this album");
        }

        albumRepository.delete(album);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private AlbumResponse createAlbumResponse(Album album) {
        AlbumResponse response = new AlbumResponse();
        response.setId(album.getId());
        response.setTitle(album.getTitle());
        response.setDescription(album.getDescription());
        response.setUserId(album.getUser().getId());
        response.setUsername(album.getUser().getUsername());
        
        List<PhotoResponse> photoResponses = album.getPhotos().stream()
                .map(photo -> {
                    PhotoResponse photoResponse = new PhotoResponse();
                    photoResponse.setId(photo.getId());
                    photoResponse.setTitle(photo.getTitle());
                    photoResponse.setDescription(photo.getDescription());
                    photoResponse.setImageUrl(photo.getImageUrl());
                    photoResponse.setTags(photo.getTags());
                    return photoResponse;
                })
                .collect(Collectors.toList());
        
        response.setPhotos(photoResponses);
        return response;
    }
} 