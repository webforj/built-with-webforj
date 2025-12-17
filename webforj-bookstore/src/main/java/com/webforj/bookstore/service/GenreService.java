package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Genre;
import com.webforj.bookstore.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing genres.
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;

    /**
     * Retrieves all genres.
     * 
     * @return a list of all genres
     */
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    /**
     * Retrieves all genres sorted by name alphabetically.
     * 
     * @return a list of genres sorted by name
     */
    public List<Genre> getAllGenresSorted() {
        return genreRepository.findAllByOrderByNameAsc();
    }

    /**
     * Retrieves a genre by its unique ID.
     * 
     * @param id the ID of the genre
     * @return an Optional containing the genre if found, or empty otherwise
     */
    public Optional<Genre> getGenreById(String id) {
        return genreRepository.findById(id);
    }

    /**
     * Retrieves a genre by its name.
     * 
     * @param name the name of the genre
     * @return an Optional containing the genre if found, or empty otherwise
     */
    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByNameIgnoreCase(name);
    }

    /**
     * Searches for genres matching the search term.
     * 
     * @param searchTerm the search term
     * @return a list of matching genres
     */
    public List<Genre> searchGenres(String searchTerm) {
        return genreRepository.findByNameContainingIgnoreCaseOrderByNameAsc(searchTerm);
    }

    /**
     * Saves a genre to the repository.
     * 
     * @param genre the genre to save
     * @return the saved genre
     */
    @Transactional
    public Genre saveGenre(Genre genre) {
        if (genre.getId() == null || genre.getId().trim().isEmpty()) {
            genre.setId(java.util.UUID.randomUUID().toString());
        }
        return genreRepository.save(genre);
    }

    /**
     * Deletes a genre by its ID.
     * 
     * @param id the ID of the genre to delete
     */
    @Transactional
    public void deleteGenre(String id) {
        genreRepository.deleteById(id);
    }

    /**
     * Returns the total count of genres.
     * 
     * @return the number of genres
     */
    public long count() {
        return genreRepository.count();
    }
}
