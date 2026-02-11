package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Genre;
import com.webforj.bookstore.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * Retrieves all genres sorted by name alphabetically.
     * 
     * @return a list of genres sorted by name
     */
    public List<Genre> getAllGenresSorted() {
        return genreRepository.findAllByOrderByNameAsc();
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
     * Returns a filterable wrapper for the repository for use with WebforJ
     * components.
     * 
     * @return a SpringDataRepository wrapper
     */
    public com.webforj.data.repository.spring.SpringDataRepository<Genre, String> getFilterableRepository() {
        return new com.webforj.data.repository.spring.SpringDataRepository<>(genreRepository);
    }
}
