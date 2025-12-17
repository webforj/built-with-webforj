package com.webforj.bookstore.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webforj.bookstore.domain.Author;
import com.webforj.bookstore.domain.Book;
import com.webforj.bookstore.domain.Genre;
import com.webforj.bookstore.domain.Publisher;
import com.webforj.bookstore.repository.AuthorRepository;
import com.webforj.bookstore.repository.BookRepository;
import com.webforj.bookstore.repository.GenreRepository;
import com.webforj.bookstore.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Data loader that initializes the database with sample data from JSON files.
 * Runs automatically on application startup.
 * 
 * @author webforJ Bookstore
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final ObjectMapper objectMapper;

    @Value("${data.books-json}")
    private Resource booksResource;

    @Value("${data.authors-json}")
    private Resource authorsResource;

    @Value("${data.genres-json}")
    private Resource genresResource;

    @Value("${data.publishers-json}")
    private Resource publishersResource;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data loading...");

        loadGenres();
        loadPublishers();
        loadAuthors();
        loadBooks();

        log.info("Data loading completed successfully!");
        log.info("Loaded {} genres, {} publishers, {} authors, {} books",
                genreRepository.count(),
                publisherRepository.count(),
                authorRepository.count(),
                bookRepository.count());
    }

    /**
     * Loads genres from the JSON resource if the repository is empty.
     * 
     * @throws IOException if an I/O error occurs reading the resource
     */
    private void loadGenres() throws IOException {
        if (genreRepository.count() == 0) {
            log.info("Loading genres from JSON...");
            List<Genre> genres = objectMapper.readValue(
                    genresResource.getInputStream(),
                    new TypeReference<List<Genre>>() {
                    });
            // Generate UUIDs for genres (JSON doesn't include IDs)
            genres.forEach(genre -> {
                if (genre.getId() == null) {
                    genre.setId(java.util.UUID.randomUUID().toString());
                }
            });
            genreRepository.saveAll(genres);
            log.info("Loaded {} genres", genres.size());
        }
    }

    /**
     * Loads publishers from the JSON resource if the repository is empty.
     * 
     * @throws IOException if an I/O error occurs reading the resource
     */
    private void loadPublishers() throws IOException {
        if (publisherRepository.count() == 0) {
            log.info("Loading publishers from JSON...");
            List<Publisher> publishers = objectMapper.readValue(
                    publishersResource.getInputStream(),
                    new TypeReference<List<Publisher>>() {
                    });
            // Generate UUIDs for publishers (JSON doesn't include IDs)
            publishers.forEach(publisher -> {
                if (publisher.getId() == null) {
                    publisher.setId(java.util.UUID.randomUUID().toString());
                }
            });
            publisherRepository.saveAll(publishers);
            log.info("Loaded {} publishers", publishers.size());
        }
    }

    /**
     * Loads authors from the JSON resource if the repository is empty.
     * 
     * @throws IOException if an I/O error occurs reading the resource
     */
    private void loadAuthors() throws IOException {
        if (authorRepository.count() == 0) {
            log.info("Loading authors from JSON...");
            List<Author> authors = objectMapper.readValue(
                    authorsResource.getInputStream(),
                    new TypeReference<List<Author>>() {
                    });
            // Generate UUIDs for authors (JSON doesn't include IDs)
            authors.forEach(author -> {
                if (author.getId() == null) {
                    author.setId(java.util.UUID.randomUUID().toString());
                }
            });
            authorRepository.saveAll(authors);
            log.info("Loaded {} authors", authors.size());
        }
    }

    /**
     * Loads books from the JSON resource if the repository is empty.
     * 
     * @throws IOException if an I/O error occurs reading the resource
     */
    private void loadBooks() throws IOException {
        if (bookRepository.count() == 0) {
            log.info("Loading books from JSON...");
            List<Book> books = objectMapper.readValue(
                    booksResource.getInputStream(),
                    new TypeReference<List<Book>>() {
                    });
            // Books already have IDs in JSON, but check anyway
            books.forEach(book -> {
                if (book.getId() == null) {
                    book.setId(java.util.UUID.randomUUID().toString());
                }
            });
            bookRepository.saveAll(books);
            log.info("Loaded {} books", books.size());
        }
    }
}
