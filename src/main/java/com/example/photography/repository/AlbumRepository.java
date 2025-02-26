package com.example.photography.repository;

import com.example.photography.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByUserId(Long userId);
} 