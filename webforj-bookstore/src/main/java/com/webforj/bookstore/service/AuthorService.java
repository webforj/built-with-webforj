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
 * @author webforJ Bookstore
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> getAllAuthorsSorted() {
        return authorRepository.findAllByOrderByNameAsc();
    }

    public Optional<Author> getAuthorById(String id) {
        return authorRepository.findById(id);
    }

    public List<Author> getAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Author> getAuthorsByNationality(String nationality) {
        return authorRepository.findByNationalityIgnoreCase(nationality);
    }

    public List<Author> searchAuthors(String searchTerm) {
        return authorRepository.searchAuthors(searchTerm);
    }

    @Transactional
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Transactional
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

    public long count() {
        return authorRepository.count();
    }
}
