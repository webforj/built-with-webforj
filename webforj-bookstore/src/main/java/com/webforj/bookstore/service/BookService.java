package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Book;
import com.webforj.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing books.
 * 
 * @author webforJ Bookstore
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Retrieves all books.
     * 
     * @return a list of all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves all books sorted by title alphabetically.
     * 
     * @return a list of books sorted by title
     */
    public List<Book> getAllBooksSortedByTitle() {
        return bookRepository.findAllByOrderByTitleAsc();
    }

    /**
     * Retrieves a book by its unique ID.
     * 
     * @param id the ID of the book
     * @return an Optional containing the book if found, or empty otherwise
     */
    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    /**
     * Retrieves a book by its ISBN.
     * 
     * @param isbn the ISBN to search for
     * @return an Optional containing the book if found, or empty otherwise
     */
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    /**
     * Retrieves books matching the given title.
     * 
     * @param title the title fragment to search for
     * @return a list of matching books
     */
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Retrieves books by a specific author.
     * 
     * @param author the author name fragment to search for
     * @return a list of matching books
     */
    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    /**
     * Retrieves books belonging to a specific genre.
     * 
     * @param genre the genre to search for
     * @return a list of books in the genre
     */
    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }

    /**
     * Retrieves books by a specific publisher.
     * 
     * @param publisher the publisher name fragment to search for
     * @return a list of matching books
     */
    public List<Book> getBooksByPublisher(String publisher) {
        return bookRepository.findByPublisherContainingIgnoreCase(publisher);
    }

    /**
     * Performs a full-text search for books.
     * 
     * @param searchTerm the search term
     * @return a list of matching books
     */
    public List<Book> searchBooks(String searchTerm) {
        return bookRepository.searchBooks(searchTerm);
    }

    /**
     * Saves a book to the repository.
     * 
     * @param book the book to save
     * @return the saved book
     */
    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Deletes a book by its ID.
     * 
     * @param id the ID of the book to delete
     */
    @Transactional
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    /**
     * Returns the total count of books.
     * 
     * @return the number of books
     */
    public long count() {
        return bookRepository.count();
    }

    /**
     * Returns a filterable wrapper for the repository for use with WebforJ
     * components.
     * 
     * @return a SpringDataRepository wrapper
     */
    public com.webforj.data.repository.spring.SpringDataRepository<Book, String> getFilterableRepository() {
        return new com.webforj.data.repository.spring.SpringDataRepository<>(bookRepository);
    }
}
