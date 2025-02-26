package com.example.photography.repository;

import com.example.photography.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByAlbumId(Long albumId);
    
    @Query("SELECT DISTINCT p FROM Photo p JOIN p.tags t WHERE t IN :tags")
    List<Photo> findByTags(Set<String> tags);
} 