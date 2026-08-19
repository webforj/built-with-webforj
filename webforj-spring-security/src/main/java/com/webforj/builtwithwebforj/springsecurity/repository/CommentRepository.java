package com.webforj.builtwithwebforj.springsecurity.repository;

import com.webforj.builtwithwebforj.springsecurity.entity.Comment;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Comment entity operations.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByTicketOrderByCreatedAtAsc(Ticket ticket);

  List<Comment> findByTicketAndIsInternalFalseOrderByCreatedAtAsc(Ticket ticket);
}
