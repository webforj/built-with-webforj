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
     * Get only active artists
     */
    public List<MusicArtist> getActiveArtists() {
        return repository.findByIsActive(true);
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
    public MusicArtist getArtistByIdOrThrow(Long id) {
        return getArtistById(id)
            .orElseThrow(() -> new IllegalArgumentException("Artist not found with ID: " + id));
    }
    
    /**
     * Search artists by name (case-insensitive)
     */
    public List<MusicArtist> searchArtistsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllArtists();
        }
        return repository.findByNameContainingIgnoreCase(name.trim());
    }
    
    /**
     * Get artists by genre
     */
    public List<MusicArtist> getArtistsByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return getAllArtists();
        }
        return repository.findByGenre(genre.trim());
    }
    
    /**
     * Get artists by country
     */
    public List<MusicArtist> getArtistsByCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            return getAllArtists();
        }
        return repository.findByCountry(country.trim());
    }

    // ========== UPDATE OPERATIONS ==========
    
    /**
     * Update an existing artist
     * Business logic: Validates data, handles name uniqueness, preserves creation data
     */
    public MusicArtist updateArtist(Long id, MusicArtist updatedArtist) {
        // Business rule: Validate input
        if (id == null || updatedArtist == null) {
            throw new IllegalArgumentException("ID and artist data cannot be null");
        }
        
        // Business rule: Artist must exist
        MusicArtist existingArtist = getArtistByIdOrThrow(id);
        
        // Business rule: Check for duplicate names (only if name changed)
        if (!existingArtist.getName().equalsIgnoreCase(updatedArtist.getName()) 
            && isArtistNameExists(updatedArtist.getName())) {
            throw new IllegalArgumentException("Artist with name '" + updatedArtist.getName() + "' already exists");
        }
        
        // Business rule: Validate year formed is not in the future
        if (updatedArtist.getYearFormed() != null && updatedArtist.getYearFormed() > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Year formed cannot be in the future");
        }
        
        // Business rule: Update only allowed fields (preserve ID and creation timestamp)
        existingArtist.setName(updatedArtist.getName().trim());
        existingArtist.setGenre(updatedArtist.getGenre() != null ? updatedArtist.getGenre().trim() : null);
        existingArtist.setCountry(updatedArtist.getCountry() != null ? updatedArtist.getCountry().trim() : null);
        existingArtist.setYearFormed(updatedArtist.getYearFormed());
        existingArtist.setIsActive(updatedArtist.getIsActive());
        existingArtist.setBiography(updatedArtist.getBiography());
        
        return repository.save(existingArtist);
    }
    
    /**
     * Toggle artist active status
     */
    public MusicArtist toggleArtistStatus(Long id) {
        MusicArtist artist = getArtistByIdOrThrow(id);
        artist.setIsActive(!artist.getIsActive());
        return repository.save(artist);
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
    
    /**
     * Soft delete - mark as inactive instead of deleting
     */
    public MusicArtist deactivateArtist(Long id) {
        MusicArtist artist = getArtistByIdOrThrow(id);
        artist.setIsActive(false);
        return repository.save(artist);
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
    
    /**
     * Get count of active artists
     */
    public long getActiveArtistsCount() {
        return repository.countByIsActive(true);
    }
    
    /**
     * Check if artist exists
     */
    public boolean artistExists(Long id) {
        return id != null && repository.existsById(id);
    }
    
    /**
     * Get artists formed in a specific year
     */
    public List<MusicArtist> getArtistsFormedInYear(Integer year) {
        if (year == null) {
            return getAllArtists();
        }
        return repository.findByYearFormed(year);
    }
    
    /**
     * Get artists formed after a specific year
     */
    public List<MusicArtist> getArtistsFormedAfter(Integer year) {
        if (year == null) {
            return getAllArtists();
        }
        return repository.findByYearFormedGreaterThan(year);
    }
}