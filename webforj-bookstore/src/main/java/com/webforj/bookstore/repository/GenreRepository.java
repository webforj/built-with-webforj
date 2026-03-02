package com.webforj.bookstore.repository;

import com.webforj.bookstore.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Genre entities.
 * 
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, String>, JpaSpecificationExecutor<Genre> {

    /**
     * Finds a genre by its name, ignoring case.
     * 
     * @param name the name to search for
     * @return an Optional containing the genre if found, or empty otherwise
     */
    Optional<Genre> findByNameIgnoreCase(String name);

    /**
     * Finds genres whose name contains the specified text, ignoring case.
     * 
     * @param name the name fragment to search for
     * @return a list of matching genres
     */
    List<Genre> findByNameContainingIgnoreCase(String name);

    /**
     * Finds genres whose name contains the specified text, ignoring case, and
     * orders them by name.
     * 
     * @param name the name fragment to search for
     * @return a list of matching genres, sorted by name
     */
    List<Genre> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    /**
     * Retrieves all genres ordered by name in ascending order.
     * 
     * @return a list of all genres, sorted by name
     */
    List<Genre> findAllByOrderByNameAsc();
}
