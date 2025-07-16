package com.webforjspring.repository;

import com.webforjspring.entity.MusicArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Custom query methods - Spring generates the SQL automatically!
    
    // Find artists by name (case-insensitive)
    List<MusicArtist> findByNameContainingIgnoreCase(String name);
    
    // Find artist by exact name (case-insensitive)
    List<MusicArtist> findByNameIgnoreCase(String name);
    
    // Find artists by genre
    List<MusicArtist> findByGenre(String genre);
    
    // Find artists by country
    List<MusicArtist> findByCountry(String country);
    
    // Find only active artists
    List<MusicArtist> findByIsActive(Boolean isActive);
    
    // Find artists by genre and active status
    List<MusicArtist> findByGenreAndIsActive(String genre, Boolean isActive);
    
    // Find artists formed in a specific year
    List<MusicArtist> findByYearFormed(Integer yearFormed);
    
    // Find artists formed after a certain year
    List<MusicArtist> findByYearFormedGreaterThan(Integer year);
    
    // Custom JPQL query example (if you need more complex queries)
    @Query("SELECT a FROM MusicArtist a WHERE a.genre = :genre AND a.yearFormed BETWEEN :startYear AND :endYear")
    List<MusicArtist> findArtistsByGenreAndYearRange(@Param("genre") String genre, 
                                                    @Param("startYear") Integer startYear, 
                                                    @Param("endYear") Integer endYear);
    
    // Find artists with no genre specified (null genre)
    List<MusicArtist> findByGenreIsNull();
    
    // Count active artists
    long countByIsActive(Boolean isActive);
}