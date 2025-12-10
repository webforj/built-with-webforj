package com.webforj.bookstore.repository;

import com.webforj.bookstore.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entities.
 * Spring Data JPA will automatically implement this interface.
 * 
 * @author webforJ Bookstore
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    /**
     * Find books by title (exact match, case-sensitive).
     */
    List<Book> findByTitle(String title);

    /**
     * Find books by title containing text (case-insensitive).
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find book by ISBN.
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Find books by author (case-insensitive).
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * Find books by publisher (case-insensitive).
     */
    List<Book> findByPublisherContainingIgnoreCase(String publisher);

    /**
     * Find books by genre (requires custom query for collection).
     */
    @Query("SELECT DISTINCT b FROM Book b JOIN b.genres g WHERE LOWER(g) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Book> findByGenreContainingIgnoreCase(@Param("genre") String genre);

    /**
     * Find all books ordered by title.
     */
    List<Book> findAllByOrderByTitleAsc();

    /**
     * Full-text search across title, author, and publisher.
     */
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);
}
