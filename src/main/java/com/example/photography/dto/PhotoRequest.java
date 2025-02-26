package com.example.photography.dto;

import java.util.Set;

public class PhotoRequest {
    private String title;
    private String description;
    private Set<String> tags;

    // Default constructor
    public PhotoRequest() {}

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getTags() {
        return tags;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
} 