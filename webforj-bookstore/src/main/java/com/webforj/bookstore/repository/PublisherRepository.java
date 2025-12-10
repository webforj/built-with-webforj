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
     * Find publisher by name (case-insensitive).
     */
    Optional<Publisher> findByNameIgnoreCase(String name);

    /**
     * Find publishers by name containing text (case-insensitive).
     */
    List<Publisher> findByNameContainingIgnoreCase(String name);

    /**
     * Find all publishers ordered by name.
     */
    List<Publisher> findAllByOrderByNameAsc();
}
