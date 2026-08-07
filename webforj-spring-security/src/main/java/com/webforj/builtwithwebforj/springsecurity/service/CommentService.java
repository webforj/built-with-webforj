package com.webforj.builtwithwebforj.springsecurity.service;

import com.webforj.builtwithwebforj.springsecurity.entity.Comment;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for comment operations.
 */
@Service
@Transactional
public class CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private TicketService ticketService;

  /**
   * Add a comment to a ticket.
   */
  public Comment addComment(Long ticketId, String text, String author, boolean isInternal) {
    Ticket ticket = ticketService.getTicketById(ticketId);
    Comment comment = new Comment(ticket, text, author, isInternal);
    return commentRepository.save(comment);
  }

  /**
   * Get all public comments for a ticket (visible to ticket owner).
   */
  @Transactional(readOnly = true)
  public List<Comment> getPublicComments(Long ticketId) {
    Ticket ticket = ticketService.getTicketById(ticketId);
    return commentRepository.findByTicketAndIsInternalFalseOrderByCreatedAtAsc(ticket);
  }

  /**
   * Get all comments for a ticket including internal notes (for support/admin).
   */
  @Transactional(readOnly = true)
  public List<Comment> getAllComments(Long ticketId) {
    Ticket ticket = ticketService.getTicketById(ticketId);
    return commentRepository.findByTicketOrderByCreatedAtAsc(ticket);
  }
}
