package com.example.photography.dto;

import java.util.List;

public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
    private String bio;
    private String profileImageUrl;
    private List<AlbumResponse> albums;

    public UserResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<AlbumResponse> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumResponse> albums) {
        this.albums = albums;
    }
} 