package com.webforj.builtwithwebforj.dashboard.models;

import jakarta.persistence.*;

@Entity
@Table(name = "news_articles")
public class NewsArticle {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String title;
  
  @Column(length = 1000)
  private String description;
  
  private String source;
  private String timeAgo;
  private String url;
  private String imageUrl;

  public NewsArticle() {
    // Default constructor required by JPA
  }

  public NewsArticle(String title, String description, String source, String timeAgo, String url) {
    this.title = title;
    this.description = description;
    this.source = source;
    this.timeAgo = timeAgo;
    this.url = url;
  }

  public NewsArticle(String title, String description, String source, String timeAgo, String url, String imageUrl) {
    this.title = title;
    this.description = description;
    this.source = source;
    this.timeAgo = timeAgo;
    this.url = url;
    this.imageUrl = imageUrl;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTimeAgo() {
    return timeAgo;
  }

  public void setTimeAgo(String timeAgo) {
    this.timeAgo = timeAgo;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}