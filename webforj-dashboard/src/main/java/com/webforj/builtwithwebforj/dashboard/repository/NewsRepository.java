package com.webforj.builtwithwebforj.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webforj.builtwithwebforj.dashboard.models.NewsArticle;

@Repository
public interface NewsRepository extends JpaRepository<NewsArticle, Long> {
    // JpaRepository provides all basic CRUD operations automatically
}