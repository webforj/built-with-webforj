package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.components.EmptyState;
import com.webforj.builtwithwebforj.springsecurity.components.PageHeader;
import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketPriority;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketStatus;
import com.webforj.builtwithwebforj.springsecurity.service.UserService;
import com.webforj.builtwithwebforj.springsecurity.service.TicketService;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.badge.BadgeTheme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.table.ColumnGroup;
import com.webforj.component.table.Table;
import com.webforj.component.table.renderer.BadgeRenderer;
import com.webforj.component.table.renderer.ConditionalRenderer;
import com.webforj.component.table.renderer.StatusDotRenderer;
import com.webforj.component.table.renderer.TextRenderer;
import com.webforj.component.table.renderer.TextRenderer.TextDecoration;
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
import java.util.EnumSet;
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
  private UserService userService;

  private final Div container = getBoundComponent();
  private String username;
  private boolean isSupport;
  private boolean isAdmin;
  private Div ticketsContainer;
  private Div statsContainer;
  private TabbedPane tabbedPane;
  private PageHeader pageHeader;
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
    String titleText = isSupport ? "Support Queue" : "Tickets";
    String subtitleText = (!isSupport && !isAdmin) ? "Manage and track your support requests" : null;

    // Create button (for non-support users or admins)
    if (!isSupport || isAdmin) {
      Button createButton = new Button("New Ticket");
      createButton.setPrefixComponent(TablerIcon.create("plus"));
      createButton.setTheme(ButtonTheme.PRIMARY);
      createButton.onClick(e -> Router.getCurrent().navigate(CreateTicketView.class));
      pageHeader = new PageHeader(titleText, subtitleText, createButton);
    } else {
      pageHeader = new PageHeader(titleText, subtitleText);
    }

    container.add(pageHeader);

    // Stats row
    statsContainer = new Div();
    statsContainer.addClassName("stat-cards");
    container.add(statsContainer);

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
    String titleText = showMyTicketsOnly ? "My Tickets" : "All Tickets";
    pageHeader.setTitle(titleText);

    // Reload tickets
    loadTickets();
  }

  private void loadTickets() {
    // Clear existing
    ticketsContainer.removeAll();
    statsContainer.removeAll();

    // Load tickets based on filter
    List<Ticket> tickets;
    if (showMyTicketsOnly) {
      User currentUser = userService.getRequiredUserByUsername(username);
      tickets = ticketService.getTicketsByUser(currentUser);
    } else {
      tickets = ticketService.getAllTickets();
    }

    // Build stat cards
    long total = tickets.size();
    long open = tickets.stream().filter(t -> t.getStatus() == TicketStatus.OPEN).count();
    long inProgress = tickets.stream().filter(t -> t.getStatus() == TicketStatus.IN_PROGRESS).count();
    long resolved = tickets.stream().filter(t -> t.getStatus() == TicketStatus.RESOLVED).count();

    statsContainer.add(
        createStatCard("ticket", String.valueOf(total), "Total", "primary"),
        createStatCard("alert-circle", String.valueOf(open), "Open", "primary"),
        createStatCard("clock", String.valueOf(inProgress), "In Progress", "warning"),
        createStatCard("circle-check", String.valueOf(resolved), "Resolved", "success")
    );

    if (tickets.isEmpty()) {
      String title = showMyTicketsOnly ? "No tickets yet" : "No tickets found";
      String message = showMyTicketsOnly
          ? "Create your first support ticket to get started"
          : "There are no tickets in the system";

      Button actionButton = null;
      if (showMyTicketsOnly && (!isSupport || isAdmin)) {
        actionButton = new Button("Create Your First Ticket");
        actionButton.setPrefixComponent(TablerIcon.create("plus"));
        actionButton.setTheme(ButtonTheme.PRIMARY);
        actionButton.onClick(e -> Router.getCurrent().navigate(CreateTicketView.class));
      }

      ticketsContainer.add(new EmptyState("ticket-off", title, message, actionButton));
      return;
    }

    // Create table
    Table<Ticket> table = new Table<>();
    table.setHeight("calc(90dvh - 260px)");
    table.setRowHeight(48);
    table.setHeaderHeight(44);
    table.setStriped(true);
    table.setBordersVisible(EnumSet.of(Table.Border.ROWS));

    // Ticket # — bold primary text
    TextRenderer<Ticket> ticketNumRenderer = new TextRenderer<>(Theme.PRIMARY);
    ticketNumRenderer.setDecorations(EnumSet.of(TextDecoration.BOLD));
    table.addColumn("ticketNumber", Ticket::getTicketNumber)
        .setLabel("Ticket #")
        .setMinWidth(120)
        .setRenderer(ticketNumRenderer);

    // Subject — takes most space
    table.addColumn("subject", Ticket::getSubject)
        .setLabel("Subject")
        .setFlex(2f);

    // Type — outlined badge
    BadgeRenderer<Ticket> typeBadge = new BadgeRenderer<>();
    typeBadge.setTheme(BadgeTheme.OUTLINED_PRIMARY);
    table.addColumn("type", ticket -> ticket.getType().getDisplayName())
        .setLabel("Type")
        .setRenderer(typeBadge);

    // Priority — status dot with theme mapping
    StatusDotRenderer<Ticket> priorityDot = new StatusDotRenderer<>();
    priorityDot.addMapping(TicketPriority.LOW.getDisplayName(), Theme.DEFAULT);
    priorityDot.addMapping(TicketPriority.MEDIUM.getDisplayName(), Theme.PRIMARY);
    priorityDot.addMapping(TicketPriority.HIGH.getDisplayName(), Theme.WARNING);
    priorityDot.addMapping(TicketPriority.URGENT.getDisplayName(), Theme.DANGER);
    table.addColumn("priority", ticket -> ticket.getPriority().getDisplayName())
        .setLabel("Priority")
        .setRenderer(priorityDot);

    // Status — conditional badge
    ConditionalRenderer<Ticket> statusRenderer = new ConditionalRenderer<>();
    statusRenderer.when("Open", new BadgeRenderer<>(BadgeTheme.PRIMARY));
    statusRenderer.when("In Progress", new BadgeRenderer<>(BadgeTheme.WARNING));
    statusRenderer.when("Resolved", new BadgeRenderer<>(BadgeTheme.SUCCESS));
    statusRenderer.when("Closed", new BadgeRenderer<>(BadgeTheme.DEFAULT));
    statusRenderer.otherwise(new BadgeRenderer<>(BadgeTheme.DEFAULT));
    table.addColumn("status", ticket -> {
      String name = ticket.getStatus().name();
      return name.substring(0, 1) + name.substring(1).toLowerCase().replace('_', ' ');
    })
        .setLabel("Status")
        .setRenderer(statusRenderer);

    // Show "Created By" column for support/admins OR when viewing all tickets
    boolean showCreatedBy = isSupport || isAdmin || !showMyTicketsOnly;
    if (showCreatedBy) {
      table.addColumn("createdBy", ticket -> ticket.getCreatedBy().getDisplayName())
          .setLabel("By");
    }

    table.addColumn("createdAt", ticket -> {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
      return ticket.getCreatedAt().format(formatter);
    })
        .setLabel("Date");

    // Column group for creation info
    if (showCreatedBy) {
      ColumnGroup createdGroup = ColumnGroup.of("created", "Created")
          .add("createdBy")
          .add("createdAt");
      table.setColumnGroups(List.of(createdGroup));
    }

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

  private Div createStatCard(String iconName, String value, String label, String theme) {
    Div card = new Div();
    card.addClassName("stat-card");

    Div iconBox = new Div();
    iconBox.addClassName("stat-card-icon stat-card-icon--" + theme);
    iconBox.add(TablerIcon.create(iconName));

    Div textBox = new Div();
    textBox.addClassName("stat-card-info");
    Span valueSpan = new Span(value);
    valueSpan.addClassName("stat-card-value");
    Span labelSpan = new Span(label);
    labelSpan.addClassName("stat-card-label");
    textBox.add(valueSpan, labelSpan);

    card.add(iconBox, textBox);
    return card;
  }
}
