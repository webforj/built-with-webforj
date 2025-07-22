package com.webforjspring.service;

import com.webforjspring.entity.MusicArtist;
import com.webforjspring.repository.MusicArtistRepository;
import com.webforj.data.repository.spring.SpringDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing music artists.
 * Provides CRUD operations and WebforJ table integration.
 */
@Service
@Transactional
public class MusicArtistService{

    @Autowired
    private MusicArtistRepository repository;

    /**
     * Creates a new music artist.
     * 
     * @param artist the artist to create
     * @return the saved artist
     */
    public MusicArtist createArtist(MusicArtist artist) {
        if (artist.getIsActive() == null) {
            artist.setIsActive(true);
        }
        
        return repository.save(artist);
    }

    /**
     * Updates an existing music artist.
     * Data binding handles field updates, so we just need to verify existence and save.
     * 
     * @param artist the artist with updated information
     * @return the updated artist
     * @throws IllegalArgumentException if artist is not found
     */
    public MusicArtist updateArtist(MusicArtist artist) {
        if (!repository.existsById(artist.getId())) {
            throw new IllegalArgumentException("Artist not found with ID: " + artist.getId());
        }
        
        return repository.save(artist);
    }

    /**
     * Deletes an artist by ID.
     * 
     * @param id the ID of the artist to delete
     * @throws IllegalArgumentException if artist is not found
     */
    public void deleteArtist(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Artist not found with ID: " + id);
        }
        
        repository.deleteById(id);
    }

    /**
     * Gets the total count of artists in the database.
     * 
     * @return the total number of artists
     */
    public long getTotalArtistsCount() {
        return repository.count();
    }
    
    /**
     * Provides a WebforJ-compatible repository wrapper for table filtering.
     * 
     * @return SpringDataRepository wrapper for the artist repository
     */
    public SpringDataRepository<MusicArtist, Long> getFilterableRepository() {
        return new SpringDataRepository<>(repository);
    }
}