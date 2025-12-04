package com.webforj.builtwithwebforj.springsecurity.service;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketStatus;
import com.webforj.builtwithwebforj.springsecurity.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for ticket operations and business logic.
 */
@Service
@Transactional
public class TicketService {

  @Autowired
  private TicketRepository ticketRepository;

  /**
   * Create a new ticket with auto-generated ticket number.
   */
  public Ticket createTicket(Ticket ticket) {
    if (ticket.getTicketNumber() == null) {
      ticket.setTicketNumber(generateTicketNumber());
    }
    return ticketRepository.save(ticket);
  }

  /**
   * Get ticket by ID.
   */
  @Transactional(readOnly = true)
  public Ticket getTicketById(Long id) {
    return ticketRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
  }

  /**
   * Get all tickets for a specific user.
   */
  @Transactional(readOnly = true)
  public List<Ticket> getTicketsByUser(User user) {
    return ticketRepository.findByCreatedByOrderByCreatedAtDesc(user);
  }

  /**
   * Get all tickets in the system (for support/admin).
   */
  @Transactional(readOnly = true)
  public List<Ticket> getAllTickets() {
    return ticketRepository.findAllByOrderByCreatedAtDesc();
  }

  /**
   * Update ticket status.
   */
  public Ticket updateTicketStatus(Long id, TicketStatus newStatus) {
    Ticket ticket = getTicketById(id);
    ticket.setStatus(newStatus);
    return ticketRepository.save(ticket);
  }

  /**
   * Update ticket.
   */
  public Ticket updateTicket(Ticket ticket) {
    return ticketRepository.save(ticket);
  }

  /**
   * Check if user owns the ticket.
   */
  @Transactional(readOnly = true)
  public boolean isUserOwner(Long ticketId, String username) {
    Ticket ticket = getTicketById(ticketId);
    return ticket.getCreatedBy().getUsername().equals(username);
  }

  /**
   * Generate ticket number in format "TKT-001", "TKT-002", etc.
   */
  private String generateTicketNumber() {
    long count = ticketRepository.count();
    return String.format("TKT-%03d", count + 1);
  }
}
