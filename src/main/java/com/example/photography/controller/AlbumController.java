package com.example.photography.controller;

import com.example.photography.dto.AlbumRequest;
import com.example.photography.dto.AlbumResponse;
import com.example.photography.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(@RequestBody AlbumRequest request) {
        return ResponseEntity.ok(albumService.createAlbum(request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<AlbumResponse>> getCurrentUserAlbums() {
        return ResponseEntity.ok(albumService.getCurrentUserAlbums());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AlbumResponse>> getUserAlbums(@PathVariable Long userId) {
        return ResponseEntity.ok(albumService.getUserAlbums(userId));
    }

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumResponse> getAlbum(@PathVariable Long albumId) {
        return ResponseEntity.ok(albumService.getAlbum(albumId));
    }

    @PutMapping("/{albumId}")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable Long albumId,
            @RequestBody AlbumRequest request) {
        return ResponseEntity.ok(albumService.updateAlbum(albumId, request));
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long albumId) {
        albumService.deleteAlbum(albumId);
        return ResponseEntity.ok().build();
    }
} 