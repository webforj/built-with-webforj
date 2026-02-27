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
 */
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface BookRepository extends JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

    /**
     * Finds books by their exact title.
     * 
     * @param title the title to search for (case-sensitive)
     * @return a list of books matching the title
     */
    List<Book> findByTitle(String title);

    /**
     * Finds books whose title contains the specified text, ignoring case.
     * 
     * @param title the title fragment to search for
     * @return a list of books with matching titles
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds a book by its ISBN.
     * 
     * @param isbn the ISBN to search for
     * @return an Optional containing the book if found, or empty otherwise
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Finds books by author name containing the specified text, ignoring case.
     * 
     * @param author the author name fragment to search for
     * @return a list of books matching the author
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * Finds books by publisher name containing the specified text, ignoring case.
     * 
     * @param publisher the publisher name fragment to search for
     * @return a list of books matching the publisher
     */
    List<Book> findByPublisherContainingIgnoreCase(String publisher);

    /**
     * Finds books containing a specific genre, ignoring case.
     * 
     * @param genre the genre to search for
     * @return a list of books containing the genre
     */
    @Query("SELECT DISTINCT b FROM Book b JOIN b.genres g WHERE LOWER(g) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Book> findByGenreContainingIgnoreCase(@Param("genre") String genre);

    /**
     * Retrieves all books ordered by title in ascending order.
     * 
     * @return a list of all books, sorted by title
     */
    List<Book> findAllByOrderByTitleAsc();

    /**
     * Performs a full-text search across title, author, and publisher fields.
     * 
     * @param searchTerm the text to search for
     * @return a list of books matching the search term in title, author, or
     *         publisher
     */
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.publisher) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);
}
