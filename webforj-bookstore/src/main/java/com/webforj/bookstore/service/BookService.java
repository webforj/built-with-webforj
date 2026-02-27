package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Book;
import com.webforj.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing books.
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

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
     * Returns a filterable wrapper for the repository for use with WebforJ
     * components.
     * 
     * @return a SpringDataRepository wrapper
     */
    public com.webforj.data.repository.spring.SpringDataRepository<Book, String> getFilterableRepository() {
        return new com.webforj.data.repository.spring.SpringDataRepository<>(bookRepository);
    }
}
