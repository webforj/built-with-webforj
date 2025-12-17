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
     * Finds authors whose name contains the specified text, ignoring case.
     * 
     * @param name the name fragment to search for
     * @return a list of matching authors
     */
    List<Author> findByNameContainingIgnoreCase(String name);

    /**
     * Finds authors by nationality, ignoring case.
     * 
     * @param nationality the nationality to search for
     * @return a list of matching authors
     */
    List<Author> findByNationalityIgnoreCase(String nationality);

    /**
     * Performs a full-text search across author's name, pen name, and full name.
     * 
     * @param searchTerm the text to search for
     * @return a list of authors matching the search term
     */
    @Query("SELECT a FROM Author a WHERE " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.penName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Author> searchAuthors(@Param("searchTerm") String searchTerm);

    /**
     * Retrieves all authors ordered by name in ascending order.
     * 
     * @return a list of all authors, sorted by name
     */
    List<Author> findAllByOrderByNameAsc();
}
