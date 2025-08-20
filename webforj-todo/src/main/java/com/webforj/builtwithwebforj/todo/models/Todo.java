package com.webforj.builtwithwebforj.todo.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    private String id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private boolean completed;

    public Todo() {
        this.id = UUID.randomUUID().toString();
        this.completed = false;
    }

    public Todo(String title) {
        this();
        this.title = title;
    }

    public Todo(String id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void toggle() {
        this.completed = !this.completed;
    }
}