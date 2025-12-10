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
 * @author webforJ Bookstore
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {

    private final GenreRepository genreRepository;

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public List<Genre> getAllGenresSorted() {
        return genreRepository.findAllByOrderByNameAsc();
    }

    public Optional<Genre> getGenreById(String id) {
        return genreRepository.findById(id);
    }

    public Optional<Genre> getGenreByName(String name) {
        return genreRepository.findByNameIgnoreCase(name);
    }

    public List<Genre> searchGenres(String searchTerm) {
        return genreRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    @Transactional
    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Transactional
    public void deleteGenre(String id) {
        genreRepository.deleteById(id);
    }

    public long count() {
        return genreRepository.count();
    }
}
