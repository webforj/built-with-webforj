package org.example.models;

public class NewsArticle {
  private String title;
  private String description;
  private String source;
  private String timeAgo;
  private String url;

  public NewsArticle(String title, String description, String source, String timeAgo, String url) {
    this.title = title;
    this.description = description;
    this.source = source;
    this.timeAgo = timeAgo;
    this.url = url;
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
}