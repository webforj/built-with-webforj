package com.webforjspring.repository;

import com.webforjspring.entity.MusicArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicArtistRepository extends JpaRepository<MusicArtist, Long>, JpaSpecificationExecutor<MusicArtist> {

    // Spring automatically provides these methods from JpaRepository:
    // - findAll() - get all artists
    // - findById(Long id) - get artist by ID
    // - save(MusicArtist artist) - save (create or update) artist
    // - deleteById(Long id) - delete artist by ID
    // - count() - count total artists
    // - existsById(Long id) - check if artist exists
    
    // JpaSpecificationExecutor provides dynamic query capabilities for filtering

    // Find artist by exact name (case-insensitive) - used for duplicate checking
    List<MusicArtist> findByNameIgnoreCase(String name);
}