package com.webforj.builtwithwebforj.springsecurity.repository;

import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Ticket entity operations.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

  List<Ticket> findByCreatedByOrderByCreatedAtDesc(User user);

  List<Ticket> findAllByOrderByCreatedAtDesc();
}
