package com.webforjspring.service;

import com.webforjspring.entity.MusicArtist;
import com.webforjspring.repository.MusicArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MusicArtistService {

    @Autowired
    private MusicArtistRepository repository;

    // ========== CREATE OPERATIONS ==========
    
    /**
     * Create a new music artist
     * Business logic: Validates data, ensures artist name is unique, sets defaults
     */
    public MusicArtist createArtist(MusicArtist artist) {
        // Business rule: Validate input
        if (artist == null) {
            throw new IllegalArgumentException("Artist cannot be null");
        }
        
        // Business rule: Check for duplicate names (case-insensitive)
        if (isArtistNameExists(artist.getName())) {
            throw new IllegalArgumentException("Artist with name '" + artist.getName() + "' already exists");
        }
        
        // Business rule: Set default values
        if (artist.getIsActive() == null) {
            artist.setIsActive(true);
        }
        
        // Business rule: Validate year formed is not in the future
        if (artist.getYearFormed() != null && artist.getYearFormed() > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Year formed cannot be in the future");
        }
        
        // Business rule: Clean up data
        artist.setName(artist.getName().trim());
        if (artist.getGenre() != null) {
            artist.setGenre(artist.getGenre().trim());
        }
        if (artist.getCountry() != null) {
            artist.setCountry(artist.getCountry().trim());
        }
        
        return repository.save(artist);
    }

    // ========== READ OPERATIONS ==========
    
    /**
     * Get all artists
     */
    public List<MusicArtist> getAllArtists() {
        return repository.findAll();
    }
    
    /**
     * Find artist by ID
     */
    public Optional<MusicArtist> getArtistById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return repository.findById(id);
    }
    
    /**
     * Find artist by ID with exception if not found
     */
    private MusicArtist getArtistByIdOrThrow(Long id) {
        return getArtistById(id)
            .orElseThrow(() -> new IllegalArgumentException("Artist not found with ID: " + id));
    }
    
    // ========== UPDATE OPERATIONS ==========
    
    /**
     * Update an existing artist
     * Business logic: Validates data, handles name uniqueness, preserves creation data
     */
    public MusicArtist updateArtist(MusicArtist artist) {
        if (artist == null || artist.getId() == null) {
            throw new IllegalArgumentException("Artist and ID cannot be null");
        }
        
        // Business rule: Artist must exist
        MusicArtist existingArtist = getArtistByIdOrThrow(artist.getId());
        
        // Business rule: Check for duplicate names (only if name changed)
        if (!existingArtist.getName().equalsIgnoreCase(artist.getName()) 
            && isArtistNameExists(artist.getName())) {
            throw new IllegalArgumentException("Artist with name '" + artist.getName() + "' already exists");
        }
        
        // Business rule: Validate year formed is not in the future
        if (artist.getYearFormed() != null && artist.getYearFormed() > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Year formed cannot be in the future");
        }
        
        // Business rule: Update only allowed fields (preserve ID and creation timestamp)
        existingArtist.setName(artist.getName().trim());
        existingArtist.setGenre(artist.getGenre() != null ? artist.getGenre().trim() : null);
        existingArtist.setCountry(artist.getCountry() != null ? artist.getCountry().trim() : null);
        existingArtist.setYearFormed(artist.getYearFormed());
        existingArtist.setIsActive(artist.getIsActive());
        existingArtist.setBiography(artist.getBiography());
        
        return repository.save(existingArtist);
    }


    // ========== DELETE OPERATIONS ==========
    
    /**
     * Delete an artist
     * Business logic: Validates existence, could add checks for related data
     */
    public void deleteArtist(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Artist ID cannot be null");
        }
        
        // Business rule: Artist must exist
        MusicArtist artist = getArtistByIdOrThrow(id);
        
        // Business rule: Could add check for related data
        // For example: if artist has albums, prevent deletion
        // if (hasAlbums(artist)) {
        //     throw new IllegalArgumentException("Cannot delete artist with existing albums");
        // }
        
        repository.deleteById(id);
    }
    

    // ========== UTILITY/HELPER METHODS ==========
    
    /**
     * Check if an artist name already exists (case-insensitive)
     */
    private boolean isArtistNameExists(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return !repository.findByNameIgnoreCase(name.trim()).isEmpty();
    }
    
    /**
     * Get total count of artists
     */
    public long getTotalArtistsCount() {
        return repository.count();
    }
    
}