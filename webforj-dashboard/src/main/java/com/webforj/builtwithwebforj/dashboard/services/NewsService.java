package com.webforj.builtwithwebforj.dashboard.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webforj.builtwithwebforj.dashboard.models.NewsArticle;
import com.webforj.builtwithwebforj.dashboard.repository.NewsRepository;

@Service
public class NewsService {
    
    private final NewsRepository repository;
    
    @Autowired
    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }
    
    public List<NewsArticle> getAllNews() {
        return repository.findAll();
    }
}