package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import jakarta.annotation.security.PermitAll;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * View for users to see their own tickets.
 * Accessible by USER and ADMIN roles only (NOT SUPPORT).
 */
@Route(value = "/tickets", outlet = MainLayout.class)
@FrameTitle("My Tickets")
@PermitAll
public class MyTicketsView extends Composite<Div> {

  private final Div container = getBoundComponent();
  private String username;
  private boolean isSupport;
  private boolean isAdmin;

  public MyTicketsView() {
    // Get current user and roles
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      username = auth.getName();
      isSupport = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_SUPPORT"));
      isAdmin = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // Support agents (who are not admins) should not access this view
    if (isSupport && !isAdmin) {
      container.add(new H1("Access Denied"));
      container.add(new Paragraph("Support agents do not have personal tickets. Please use the Support Queue."));
      return;
    }

    setupContent();
  }

  private void setupContent() {
    container.addClassName("my-tickets-view");
    container.setStyle("padding", "2rem");

    // Show different message based on role
    H1 title = new H1("My Tickets - " + username);
    container.add(title);

    if (isAdmin) {
      Paragraph roleInfo = new Paragraph("(Viewing as ADMIN - you can also access Support Queue and Admin Dashboard)");
      roleInfo.setStyle("color", "#666");
      container.add(roleInfo);
    } else {
      Paragraph roleInfo = new Paragraph("(Viewing as USER - you can only see your own tickets)");
      roleInfo.setStyle("color", "#666");
      container.add(roleInfo);
    }

    // TODO: Display table of user's tickets from TicketService
    Paragraph placeholder = new Paragraph("Ticket list will be displayed here...");
    placeholder.setStyle("margin-top", "2rem");
    container.add(placeholder);
  }
}
