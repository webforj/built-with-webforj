package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Author;
import com.webforj.bookstore.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * Retrieves all authors sorted by name alphabetically.
     * 
     * @return a list of authors sorted by name
     */
    public List<Author> getAllAuthorsSorted() {
        return authorRepository.findAllByOrderByNameAsc();
    }
}
