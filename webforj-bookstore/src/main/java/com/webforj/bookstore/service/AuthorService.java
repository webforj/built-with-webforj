package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Author;
import com.webforj.bookstore.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing authors.
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    /**
     * Retrieves all authors.
     * 
     * @return a list of all authors
     */
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Retrieves all authors sorted by name alphabetically.
     * 
     * @return a list of authors sorted by name
     */
    public List<Author> getAllAuthorsSorted() {
        return authorRepository.findAllByOrderByNameAsc();
    }

    /**
     * Retrieves an author by their unique ID.
     * 
     * @param id the ID of the author
     * @return an Optional containing the author if found, or empty otherwise
     */
    public Optional<Author> getAuthorById(String id) {
        return authorRepository.findById(id);
    }

    /**
     * Retrieves authors matching the given name.
     * 
     * @param name the name fragment to search for
     * @return a list of matching authors
     */
    public List<Author> getAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Retrieves authors by nationality.
     * 
     * @param nationality the nationality to search for
     * @return a list of matching authors
     */
    public List<Author> getAuthorsByNationality(String nationality) {
        return authorRepository.findByNationalityIgnoreCase(nationality);
    }

    /**
     * Performs a full-text search for authors.
     * 
     * @param searchTerm the search term
     * @return a list of matching authors
     */
    public List<Author> searchAuthors(String searchTerm) {
        return authorRepository.searchAuthors(searchTerm);
    }

    /**
     * Saves an author to the repository.
     * 
     * @param author the author to save
     * @return the saved author
     */
    @Transactional
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    /**
     * Deletes an author by their ID.
     * 
     * @param id the ID of the author to delete
     */
    @Transactional
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

    /**
     * Returns the total count of authors.
     * 
     * @return the number of authors
     */
    public long count() {
        return authorRepository.count();
    }
}
