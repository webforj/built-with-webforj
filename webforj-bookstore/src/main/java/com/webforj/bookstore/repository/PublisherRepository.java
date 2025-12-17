package com.webforj.bookstore.repository;

import com.webforj.bookstore.domain.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Publisher entities.
 * 
 * @author webforJ Bookstore
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, String> {

    /**
     * Finds a publisher by its name, ignoring case.
     * 
     * @param name the name to search for
     * @return an Optional containing the publisher if found, or empty otherwise
     */
    Optional<Publisher> findByNameIgnoreCase(String name);

    /**
     * Finds publishers whose name contains the specified text, ignoring case.
     * 
     * @param name the name fragment to search for
     * @return a list of matching publishers
     */
    List<Publisher> findByNameContainingIgnoreCase(String name);

    /**
     * Retrieves all publishers ordered by name in ascending order.
     * 
     * @return a list of all publishers, sorted by name
     */
    List<Publisher> findAllByOrderByNameAsc();
}
