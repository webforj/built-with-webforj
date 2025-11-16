package com.webforj.builtwithwebforj.springsecurity.entity.ticket;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.webforj.builtwithwebforj.springsecurity.entity.Comment;
import com.webforj.builtwithwebforj.springsecurity.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Support ticket entity.
 *
 * Note: Future enhancements could include:
 * - assignedTo field for ticket assignment to support agents
 * - updatedAt timestamp for tracking last modification
 */
@Entity
@Table(name = "tickets")
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 20)
  private String ticketNumber; // "TKT-001" format

  @Column(nullable = false, length = 200)
  private String subject;

  @Column(nullable = false, length = 2000)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TicketStatus status = TicketStatus.OPEN;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TicketType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TicketPriority priority;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "created_by_id", nullable = false)
  private User createdBy;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createdAt ASC")
  private List<Comment> comments = new ArrayList<>();

  public Ticket() {
  }

  public Ticket(String ticketNumber, String subject, String description, TicketType type,
      TicketPriority priority, User createdBy) {
    this.ticketNumber = ticketNumber;
    this.subject = subject;
    this.description = description;
    this.type = type;
    this.priority = priority;
    this.createdBy = createdBy;
  }

  // Helper method to add comment
  public void addComment(Comment comment) {
    comments.add(comment);
    comment.setTicket(this);
  }

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTicketNumber() {
    return ticketNumber;
  }

  public void setTicketNumber(String ticketNumber) {
    this.ticketNumber = ticketNumber;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TicketStatus getStatus() {
    return status;
  }

  public void setStatus(TicketStatus status) {
    this.status = status;
  }

  public TicketType getType() {
    return type;
  }

  public void setType(TicketType type) {
    this.type = type;
  }

  public TicketPriority getPriority() {
    return priority;
  }

  public void setPriority(TicketPriority priority) {
    this.priority = priority;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Ticket ticket = (Ticket) o;
    return Objects.equals(ticketNumber, ticket.ticketNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(ticketNumber);
  }
}
