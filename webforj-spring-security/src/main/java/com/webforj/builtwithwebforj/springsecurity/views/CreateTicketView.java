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
 * View for creating new support tickets.
 * Accessible by USER and ADMIN roles only (NOT SUPPORT).
 */
@Route(value = "/tickets/new", outlet = MainLayout.class)
@FrameTitle("Create Ticket")
@PermitAll
public class CreateTicketView extends Composite<Div> {

  private final Div container = getBoundComponent();
  private String username;
  private boolean isSupport;
  private boolean isAdmin;

  public CreateTicketView() {
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
      container.add(new Paragraph("Support agents cannot create tickets. Only users can submit tickets."));
      return;
    }

    setupContent();
  }

  private void setupContent() {
    container.addClassName("create-ticket-view");
    container.setStyle("padding", "2rem");

    H1 title = new H1("Create New Ticket");
    container.add(title);

    if (isAdmin) {
      Paragraph roleInfo = new Paragraph("(Creating ticket as ADMIN - " + username + ")");
      roleInfo.setStyle("color", "#666");
      container.add(roleInfo);
    } else {
      Paragraph roleInfo = new Paragraph("(Creating ticket as USER - " + username + ")");
      roleInfo.setStyle("color", "#666");
      container.add(roleInfo);
    }

    // TODO: Add form fields for ticket creation
    Paragraph placeholder = new Paragraph("Ticket creation form will be displayed here...");
    placeholder.setStyle("margin-top", "2rem");
    container.add(placeholder);
  }
}
