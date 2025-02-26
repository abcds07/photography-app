package com.example.photography.controller;

import com.example.photography.dto.PhotoRequest;
import com.example.photography.dto.PhotoResponse;
import com.example.photography.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/{albumId}")
    public ResponseEntity<PhotoResponse> uploadPhoto(
            @PathVariable Long albumId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("request") String requestJson) throws Exception {
        
        // Convert request JSON string to PhotoRequest object
        ObjectMapper mapper = new ObjectMapper();
        PhotoRequest request = mapper.readValue(requestJson, PhotoRequest.class);
        
        PhotoResponse response = photoService.uploadPhoto(albumId, file, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<PhotoResponse>> getAlbumPhotos(@PathVariable Long albumId) {
        return ResponseEntity.ok(photoService.getAlbumPhotos(albumId));
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> getPhoto(@PathVariable Long photoId) {
        return ResponseEntity.ok(photoService.getPhoto(photoId));
    }

    @PutMapping("/{photoId}")
    public ResponseEntity<PhotoResponse> updatePhoto(
            @PathVariable Long photoId,
            @RequestBody PhotoRequest request) {
        return ResponseEntity.ok(photoService.updatePhoto(photoId, request));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) throws IOException {
        photoService.deletePhoto(photoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhotoResponse>> searchPhotosByTags(@RequestParam Set<String> tags) {
        return ResponseEntity.ok(photoService.searchPhotosByTags(tags));
    }
} 