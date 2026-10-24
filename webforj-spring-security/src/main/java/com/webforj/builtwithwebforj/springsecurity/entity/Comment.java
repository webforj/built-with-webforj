package com.webforj.builtwithwebforj.springsecurity.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Comment or note on a support ticket.
 * Can be either public (visible to ticket owner) or internal (visible only to support/admin).
 */
@Entity
@Table(name = "comments")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_id", nullable = false)
  private Ticket ticket;

  @Column(nullable = false, length = 2000)
  private String text;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private boolean isInternal = false;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public Comment() {
  }

  public Comment(Ticket ticket, String text, String author, boolean isInternal) {
    this.ticket = ticket;
    this.text = text;
    this.author = author;
    this.isInternal = isInternal;
  }

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public String getAuthor() {
    return author;
  }

  public boolean isInternal() {
    return isInternal;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Comment comment = (Comment) o;
    return Objects.equals(id, comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
