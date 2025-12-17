package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Publisher;
import com.webforj.bookstore.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing publishers.
 * 
 * @author webforJ Bookstore
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherService {

    private final PublisherRepository publisherRepository;

    /**
     * Retrieves all publishers.
     * 
     * @return a list of all publishers
     */
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    /**
     * Retrieves all publishers sorted by name alphabetically.
     * 
     * @return a list of publishers sorted by name
     */
    public List<Publisher> getAllPublishersSorted() {
        return publisherRepository.findAllByOrderByNameAsc();
    }

    /**
     * Retrieves a publisher by their unique ID.
     * 
     * @param id the ID of the publisher
     * @return an Optional containing the publisher if found, or empty otherwise
     */
    public Optional<Publisher> getPublisherById(String id) {
        return publisherRepository.findById(id);
    }

    /**
     * Retrieves a publisher by their name.
     * 
     * @param name the name of the publisher
     * @return an Optional containing the publisher if found, or empty otherwise
     */
    public Optional<Publisher> getPublisherByName(String name) {
        return publisherRepository.findByNameIgnoreCase(name);
    }

    /**
     * Searches for publishers matching the search term.
     * 
     * @param searchTerm the search term
     * @return a list of matching publishers
     */
    public List<Publisher> searchPublishers(String searchTerm) {
        return publisherRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    /**
     * Saves a publisher to the repository.
     * 
     * @param publisher the publisher to save
     * @return the saved publisher
     */
    @Transactional
    public Publisher savePublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    /**
     * Deletes a publisher by their ID.
     * 
     * @param id the ID of the publisher to delete
     */
    @Transactional
    public void deletePublisher(String id) {
        publisherRepository.deleteById(id);
    }

    /**
     * Returns the total count of publishers.
     * 
     * @return the number of publishers
     */
    public long count() {
        return publisherRepository.count();
    }
}
