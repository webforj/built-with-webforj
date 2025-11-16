package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;

import com.webforj.builtwithwebforj.springsecurity.RequireTicketOwnership;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * View for displaying ticket details.
 * Adapts UI based on user role:
 * - Regular users: Can only view their own tickets (enforced by @RequireTicketOwnership)
 * - Support/Admin: Can view any ticket and see additional controls
 *
 * Security is enforced by the TicketOwnershipEvaluator which checks:
 * - If user has SUPPORT or ADMIN role → grant access to any ticket
 * - If user is the ticket owner → grant access
 * - Otherwise → deny access
 */
@Route(value = "/tickets/:id", outlet = MainLayout.class)
@FrameTitle("Ticket Detail")
@RequireTicketOwnership("id")
public class TicketDetailView extends Composite<Div> implements DidEnterObserver {

  private final Div container = getBoundComponent();
  private String username;
  private boolean isSupport;
  private boolean isAdmin;
  private String ticketId;

  public TicketDetailView() {
    container.addClassName("ticket-detail-view");
    container.setStyle("padding", "2rem");
  }

  @Override
  public void onDidEnter(DidEnterEvent event, ParametersBag params) {
    // Get ticket ID from route parameters
    ticketId = params.get("id").orElse("unknown");

    // Get current user and roles
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      username = auth.getName();
      isSupport = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_SUPPORT"));
      isAdmin = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    setupContent();
  }

  private void setupContent() {
    // Clear any existing content
    container.removeAll();

    H1 title = new H1("Ticket #" + ticketId);
    container.add(title);

    // Show different information based on role
    if (isAdmin) {
      Paragraph roleInfo = new Paragraph("(Viewing as ADMIN - Full access to all tickets and controls)");
      roleInfo.setStyle("color", "#4A9EFF");
      container.add(roleInfo);

      Paragraph features = new Paragraph("You can see: All comments (including internal notes), Change status, Assign ticket");
      features.setStyle("margin-top", "1rem");
      container.add(features);
    } else if (isSupport) {
      Paragraph roleInfo = new Paragraph("(Viewing as SUPPORT - Can view all tickets and add internal notes)");
      roleInfo.setStyle("color", "#FF9A3C");
      container.add(roleInfo);

      Paragraph features = new Paragraph("You can see: All comments (including internal notes), Change status, Assign ticket");
      features.setStyle("margin-top", "1rem");
      container.add(features);
    } else {
      Paragraph roleInfo = new Paragraph("(Viewing as USER - " + username + ")");
      roleInfo.setStyle("color", "#666");
      container.add(roleInfo);

      Paragraph features = new Paragraph("You can see: Your own ticket details, Public comments only, Can add comments and close ticket");
      features.setStyle("margin-top", "1rem");
      container.add(features);
    }

    // TODO: Load actual ticket data from TicketService
    // TODO: Check ownership for regular users (will be handled by @RequireTicketOwnership in Phase 3)
    Paragraph placeholder = new Paragraph("Ticket details, description, and comments will be displayed here...");
    placeholder.setStyle("margin-top", "2rem");
    container.add(placeholder);
  }
}
