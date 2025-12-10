package com.webforj.bookstore.repository;

import com.webforj.bookstore.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Author entities.
 * 
 * @author webforJ Bookstore
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    /**
     * Find authors by name (case-insensitive).
     */
    List<Author> findByNameContainingIgnoreCase(String name);

    /**
     * Find authors by nationality.
     */
    List<Author> findByNationalityIgnoreCase(String nationality);

    /**
     * Full-text search across name, penName, and fullName.
     */
    @Query("SELECT a FROM Author a WHERE " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.penName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Author> searchAuthors(@Param("searchTerm") String searchTerm);

    /**
     * Find all authors ordered by name.
     */
    List<Author> findAllByOrderByNameAsc();
}
