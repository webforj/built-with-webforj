package com.webforj.bookstore.service;

import com.webforj.bookstore.domain.Publisher;
import com.webforj.bookstore.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing publishers.
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherService {

    private final PublisherRepository publisherRepository;

    /**
     * Retrieves all publishers sorted by name alphabetically.
     * 
     * @return a list of publishers sorted by name
     */
    public List<Publisher> getAllPublishersSorted() {
        return publisherRepository.findAllByOrderByNameAsc();
    }
}
