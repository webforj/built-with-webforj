package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.renderers.ticket.PriorityRenderer;
import com.webforj.builtwithwebforj.springsecurity.renderers.ticket.StatusBadgeRenderer;
import com.webforj.builtwithwebforj.springsecurity.renderers.ticket.TicketNumberRenderer;
import com.webforj.builtwithwebforj.springsecurity.renderers.ticket.TypeBadgeRenderer;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import com.webforj.builtwithwebforj.springsecurity.service.TicketService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.table.Table;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.ParametersBag;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main dashboard view for authenticated users.
 * Displays tickets with tabs for regular users (My Tickets/All Tickets)
 * and unified view for SUPPORT/ADMIN.
 */
@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Dashboard")
@PermitAll
@StyleSheet("ws://dashboard.css")
public class DashboardView extends Composite<Div> {

  @Autowired
  private TicketService ticketService;

  @Autowired
  private UserRepository userRepository;

  private final Div container = getBoundComponent();
  private String username;
  private boolean isSupport;
  private boolean isAdmin;
  private Div ticketsContainer;
  private TabbedPane tabbedPane;
  private boolean showMyTicketsOnly = false;

  public DashboardView() {
    // Constructor - dependencies not yet injected
  }

  @PostConstruct
  private void init() {
    // Get current user and roles
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      username = auth.getName();
      isSupport = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_SUPPORT"));
      isAdmin = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // Default view: My Tickets for regular users, All Tickets for SUPPORT/ADMIN
    showMyTicketsOnly = !isSupport && !isAdmin;

    setupContent();
  }

  private void setupContent() {
    container.addClassName("view-container");

    // Page header
    FlexLayout header = FlexLayout.create()
        .horizontal()
        .justify().between()
        .align().center()
        .build();
    header.addClassName("page-header");

    // Title section
    Div titleSection = new Div();
    String titleText = isSupport ? "Support Queue" : "Tickets";
    H1 title = new H1(titleText);
    title.addClassName("page-title");
    titleSection.add(title);

    if (!isSupport && !isAdmin) {
      Paragraph subtitle = new Paragraph("Manage and track your support requests");
      subtitle.addClassName("page-subtitle");
      titleSection.add(subtitle);
    }

    header.add(titleSection);

    // Create button
    if (!isSupport || isAdmin) {
      Button createButton = new Button("New Ticket");
      createButton.setPrefixComponent(TablerIcon.create("plus"));
      createButton.setTheme(ButtonTheme.PRIMARY);
      createButton.onClick(e -> Router.getCurrent().navigate(CreateTicketView.class));
      header.add(createButton);
    }

    container.add(header);

    // Tabs for regular users
    if (!isSupport && !isAdmin) {
      tabbedPane = new TabbedPane();
      tabbedPane.addClassName("dashboard-tabs");
      tabbedPane.setPlacement(TabbedPane.Placement.TOP);
      tabbedPane.setAlignment(TabbedPane.Alignment.CENTER);
      tabbedPane.setBorderless(true);

      tabbedPane.addTab("My Tickets");
      tabbedPane.addTab("All Tickets");
      tabbedPane.select(showMyTicketsOnly ? 0 : 1);

      tabbedPane.onSelect(event -> {
        showMyTicketsOnly = event.getTab().getText().equals("My Tickets");
        refreshTickets();
      });

      container.add(tabbedPane);
    }

    // Tickets container
    ticketsContainer = new Div();
    container.add(ticketsContainer);

    loadTickets();
  }

  private void refreshTickets() {
    // Update title
    String titleText;
    if (showMyTicketsOnly) {
      titleText = "My Tickets";
    } else {
      titleText = "All Tickets";
    }

    // Find and update the H1 title
    container.getComponents().stream()
        .filter(c -> c instanceof FlexLayout)
        .findFirst()
        .ifPresent(header -> {
          ((FlexLayout) header).getComponents().stream()
              .filter(c -> c instanceof H1)
              .findFirst()
              .ifPresent(h1 -> ((H1) h1).setText(titleText));
        });

    // Reload tickets
    loadTickets();
  }

  private void loadTickets() {
    // Clear existing tickets
    ticketsContainer.removeAll();

    // Load tickets based on filter
    List<Ticket> tickets;
    if (showMyTicketsOnly) {
      // Load only user's tickets
      User currentUser = userRepository.findByUsername(username)
          .orElseThrow(() -> new RuntimeException("User not found: " + username));
      tickets = ticketService.getTicketsByUser(currentUser);
    } else {
      // Load all tickets
      tickets = ticketService.getAllTickets();
    }

    if (tickets.isEmpty()) {
      Div emptyState = new Div();
      emptyState.addClassName("empty-state");

      // Large icon illustration
      Div iconWrapper = new Div();
      iconWrapper.addClassName("empty-state-icon");
      iconWrapper.add(TablerIcon.create("ticket-off"));
      emptyState.add(iconWrapper);

      H1 emptyTitle = new H1(showMyTicketsOnly ? "No tickets yet" : "No tickets found");
      emptyTitle.addClassName("empty-state-title");
      emptyState.add(emptyTitle);

      Paragraph emptyMessage = new Paragraph(
          showMyTicketsOnly
            ? "Create your first support ticket to get started"
            : "There are no tickets in the system");
      emptyMessage.addClassName("empty-state-message");
      emptyState.add(emptyMessage);

      if (showMyTicketsOnly && (!isSupport || isAdmin)) {
        Button createFirstTicket = new Button("Create Your First Ticket");
        createFirstTicket.setPrefixComponent(TablerIcon.create("plus"));
        createFirstTicket.setTheme(ButtonTheme.PRIMARY);
        createFirstTicket.onClick(e -> Router.getCurrent().navigate(CreateTicketView.class));
        emptyState.add(createFirstTicket);
      }

      ticketsContainer.add(emptyState);
      return;
    }

    // Create table
    Table<Ticket> table = new Table<>();
    table.addClassName("tickets-table");
    table.setHeight("calc(80dvh - 280px)");

    // Add columns
    table.addColumn("ticketNumber", Ticket::getTicketNumber)
        .setLabel("Ticket #")
        .setRenderer(new TicketNumberRenderer());

    table.addColumn("subject", Ticket::getSubject)
        .setLabel("Subject");

    // Hidden columns for type data
    table.addColumn("typeDisplay", ticket -> ticket.getType().getDisplayName()).setHidden(true);

    // Type column with renderer
    table.addColumn("type", ticket -> ticket.getType().name())
        .setLabel("Type")
        .setRenderer(new TypeBadgeRenderer());

    // Hidden columns for priority data
    table.addColumn("priorityDisplay", ticket -> ticket.getPriority().getDisplayName()).setHidden(true);
    table.addColumn("priorityColor", ticket -> ticket.getPriority().getColor()).setHidden(true);

    // Priority column with renderer
    table.addColumn("priority", new PriorityRenderer())
        .setLabel("Priority");

    table.addColumn("status", Ticket::getStatus)
        .setLabel("Status")
        .setRenderer(new StatusBadgeRenderer());

    // Show "Created By" column for support/admins OR when viewing all tickets
    if (isSupport || isAdmin || !showMyTicketsOnly) {
      table.addColumn("createdBy", ticket -> ticket.getCreatedBy().getDisplayName())
          .setLabel("Created By");
    }

    table.addColumn("createdAt", ticket -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
      return ticket.getCreatedAt().format(formatter);
    })
        .setLabel("Created");

    // Set table data
    table.setItems(tickets);
    table.setColumnsToAutoSize();

    // Handle click to navigate to ticket detail (single click for better UX)
    table.onItemClick(event -> {
      Ticket clickedTicket = event.getItem();
      Router.getCurrent().navigate(TicketDetailView.class,
          ParametersBag.of("id=" + clickedTicket.getId().toString()));
    });

    ticketsContainer.add(table);
  }
}
