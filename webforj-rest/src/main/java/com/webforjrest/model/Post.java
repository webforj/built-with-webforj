package com.webforjrest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model representing a Post from JSONPlaceholder API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String body;

    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Returns truncated body text for table display.
     * Limits to 100 characters with ellipsis.
     */
    public String getBodyTruncated() {
        if (body == null) {
            return "";
        }
        if (body.length() <= 100) {
            return body;
        }
        return body.substring(0, 100) + "...";
    }
}
