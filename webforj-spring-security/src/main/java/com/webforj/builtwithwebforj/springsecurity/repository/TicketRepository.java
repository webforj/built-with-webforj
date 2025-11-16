package com.webforj.builtwithwebforj.springsecurity.repository;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Ticket entity operations.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  Optional<Ticket> findByTicketNumber(String ticketNumber);

  List<Ticket> findByCreatedByOrderByCreatedAtDesc(User user);

  List<Ticket> findByStatusOrderByCreatedAtDesc(TicketStatus status);

  List<Ticket> findAllByOrderByCreatedAtDesc();
}
