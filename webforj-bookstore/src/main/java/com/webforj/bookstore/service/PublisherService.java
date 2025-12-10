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

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    public List<Publisher> getAllPublishersSorted() {
        return publisherRepository.findAllByOrderByNameAsc();
    }

    public Optional<Publisher> getPublisherById(String id) {
        return publisherRepository.findById(id);
    }

    public Optional<Publisher> getPublisherByName(String name) {
        return publisherRepository.findByNameIgnoreCase(name);
    }

    public List<Publisher> searchPublishers(String searchTerm) {
        return publisherRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    @Transactional
    public Publisher savePublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @Transactional
    public void deletePublisher(String id) {
        publisherRepository.deleteById(id);
    }

    public long count() {
        return publisherRepository.count();
    }
}
