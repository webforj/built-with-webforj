package com.webforj.bookstore.repository;

import com.webforj.bookstore.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Genre entities.
 * 
 * @author webforJ Bookstore
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, String> {

    /**
     * Find genre by name (case-insensitive).
     */
    Optional<Genre> findByNameIgnoreCase(String name);

    /**
     * Find genres by name containing text (case-insensitive).
     */
    List<Genre> findByNameContainingIgnoreCase(String name);

    /**
     * Find all genres ordered by name.
     */
    List<Genre> findAllByOrderByNameAsc();
}
