package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import jakarta.annotation.security.PermitAll;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Main dashboard view for authenticated users.
 * Displays welcome message and quick stats.
 */
@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Dashboard")
@PermitAll
public class DashboardView extends Composite<Div> {

  private final Div container = getBoundComponent();
  private String username;

  public DashboardView() {
    // Get current user information
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      username = auth.getName();
    }

    setupContent();
  }

  private void setupContent() {
    container.addClassName("dashboard-view");
    container.setStyle("padding", "2rem");

    // Welcome message with username
    H1 welcomeMessage = new H1("Welcome, " + username + "!");
    container.add(welcomeMessage);

    // TODO: Add ticket statistics cards, quick actions, recent activity
  }
}
