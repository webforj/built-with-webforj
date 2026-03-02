package com.webforj.bookstore.config;

import com.github.javafaker.Faker;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Data loader that initializes the database with sample data using Java Faker.
 * Runs automatically on application startup.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;

    private final Faker faker = new Faker(new Locale("en"));

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data generation...");

        if (genreRepository.count() == 0) {
            loadGenres();
        }
        if (publisherRepository.count() == 0) {
            loadPublishers();
        }
        if (authorRepository.count() == 0) {
            loadAuthors();
        }
        if (bookRepository.count() == 0) {
            loadBooks();
        }

        log.info("Data generation completed successfully!");
        log.info("Loaded {} genres, {} publishers, {} authors, {} books",
                genreRepository.count(),
                publisherRepository.count(),
                authorRepository.count(),
                bookRepository.count());
    }

    private void loadGenres() {
        log.info("Generating genres...");
        List<Genre> genres = new ArrayList<>();
        // Hardcoded list of common genres to ensure quality names
        String[] genreNames = {
                "Fiction", "Science Fiction", "Mystery", "Fantasy", "Romance",
                "Thriller", "Horror", "Non-fiction", "History", "Biography",
                "Business", "Cooking", "Travel", "Self-help", "Health"
        };

        for (String name : genreNames) {
            Genre genre = new Genre();
            genre.setId(UUID.randomUUID().toString());
            genre.setName(name);
            genre.setColor(getRandomRgbaColor());
            genres.add(genre);
        }
        genreRepository.saveAll(genres);
        log.info("Generated {} genres", genres.size());
    }

    private void loadPublishers() {
        log.info("Generating publishers...");
        List<Publisher> publishers = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();
        int targetCount = 10;
        int attempts = 0;

        while (publishers.size() < targetCount && attempts < 100) {
            String name = faker.book().publisher();
            if (usedNames.add(name)) {
                Publisher publisher = new Publisher();
                publisher.setId(UUID.randomUUID().toString());
                publisher.setName(name);
                publishers.add(publisher);
            }
            attempts++;
        }
        publisherRepository.saveAll(publishers);
        log.info("Generated {} publishers", publishers.size());
    }

    private void loadAuthors() {
        log.info("Generating authors...");
        List<Author> authors = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();
        int targetCount = 20;
        int attempts = 0;

        while (authors.size() < targetCount && attempts < 200) {
            String name = faker.book().author();
            if (usedNames.add(name)) {
                Author author = new Author();
                author.setId(UUID.randomUUID().toString());
                author.setName(name);
                authors.add(author);
            }
            attempts++;
        }
        authorRepository.saveAll(authors);
        log.info("Generated {} authors", authors.size());
    }

    private void loadBooks() {
        log.info("Generating books...");
        List<Book> books = new ArrayList<>();
        List<Author> authors = authorRepository.findAll();
        List<Publisher> publishers = publisherRepository.findAll();
        List<Genre> allGenres = genreRepository.findAll();
        Set<String> usedTitles = new HashSet<>();

        int targetCount = 50;
        int attempts = 0;

        while (books.size() < targetCount && attempts < 500) {
            String title = faker.book().title();
            if (usedTitles.add(title)) {
                Book book = new Book();
                book.setId(UUID.randomUUID().toString());
                book.setTitle(title);
                book.setIsbn(faker.code().isbn13());
                book.setLanguage("English");
                book.setNotes(faker.lorem().paragraph());

                // Randomly assign simple fields
                if (!authors.isEmpty()) {
                    book.setAuthor(authors.get(faker.random().nextInt(authors.size())).getName());
                }
                if (!publishers.isEmpty()) {
                    book.setPublisher(publishers.get(faker.random().nextInt(publishers.size())).getName());
                }

                // Random genres (1 to 3)
                int genreCount = faker.random().nextInt(1, 4);
                List<String> bookGenres = new ArrayList<>();
                if (!allGenres.isEmpty()) {
                    for (int j = 0; j < genreCount; j++) {
                        String genreName = allGenres.get(faker.random().nextInt(allGenres.size())).getName();
                        if (!bookGenres.contains(genreName)) {
                            bookGenres.add(genreName);
                        }
                    }
                }
                book.setGenres(bookGenres);

                // Correctly formatted LocalDate
                book.setPublicationDate(faker.date().past(36500, TimeUnit.DAYS)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                books.add(book);
            }
            attempts++;
        }
        bookRepository.saveAll(books);
        log.info("Generated {} books", books.size());
    }

    private String getRandomRgbaColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return String.format("rgba(%d, %d, %d, 0.8)", r, g, b);
    }
}
