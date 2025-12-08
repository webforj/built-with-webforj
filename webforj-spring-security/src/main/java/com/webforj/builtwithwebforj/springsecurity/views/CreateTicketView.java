package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketPriority;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketStatus;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketType;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import com.webforj.builtwithwebforj.springsecurity.service.TicketService;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextArea;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.toast.Toast;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.ParametersBag;

import jakarta.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * View for creating new support tickets.
 * Accessible by USER and ADMIN roles only (NOT SUPPORT).
 */
@Route(value = "/tickets/new", outlet = MainLayout.class)
@FrameTitle("Create Ticket")
@PermitAll
@StyleSheet("ws://create-ticket.css")
public class CreateTicketView extends Composite<Div> {

  @Autowired
  private TicketService ticketService;

  @Autowired
  private UserRepository userRepository;

  private final Div container = getBoundComponent();
  private String username;
  private boolean isSupport;
  private boolean isAdmin;

  // Form fields - names must match Ticket entity property names for automatic binding
  private TextField subject;
  private ChoiceBox type;
  private ChoiceBox priority;
  private TextArea description;
  private Button submitButton;
  private Button cancelButton;

  // Data binding context
  private BindingContext<Ticket> context;
  private Ticket ticket = new Ticket();

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
    container.addClassName("view-container");
    container.addClassName("form-view");

    // Back button
    Button backButton = new Button("Back");
    backButton.setPrefixComponent(TablerIcon.create("arrow-left"));
    backButton.setTheme(ButtonTheme.DEFAULT);
    backButton.addClassName("back-button");
    backButton.onClick(e -> Router.getCurrent().navigate(DashboardView.class));
    container.add(backButton);

    // Page header
    Div header = new Div();
    header.addClassName("page-header");

    H1 title = new H1("Create New Ticket");
    title.addClassName("page-title");

    Paragraph subtitle = new Paragraph("Submit a new support request");
    subtitle.addClassName("page-subtitle");

    header.add(title, subtitle);
    container.add(header);

    // Create form
    createForm();

    // Setup data binding
    setupDataBinding();
  }

  private void setupDataBinding() {
    // Enable automatic binding with Jakarta validation
    context = BindingContext.of(this, Ticket.class, true);

    // Listen for validation changes - auto enable/disable submit button
    context.addValidateListener(e -> {
      submitButton.setEnabled(e.isValid());
    });

    // Read initial values into form (priority has default value)
    context.read(ticket);
  }

  private void createForm() {
    Div formCard = new Div();
    formCard.addClassName("card");

    FlexLayout formContainer = new FlexLayout();
    formContainer.setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-l)");

    // Subject field
    subject = new TextField();
    subject.setLabel("Subject");
    subject.setPlaceholder("Brief description of the issue or request");
    formContainer.add(subject);

    // Type and Priority row
    FlexLayout row = FlexLayout.create().horizontal().build();
    row.setSpacing("var(--dwc-space-m)");

    type = new ChoiceBox();
    type.setLabel("Type");
    for (TicketType t : TicketType.values()) {
      type.add(t, t.getDisplayName());
    }
    type.addClassName("flex-1");

    priority = new ChoiceBox();
    priority.setLabel("Priority");
    for (TicketPriority p : TicketPriority.values()) {
      priority.add(p, p.getDisplayName());
    }
    priority.addClassName("flex-1");

    row.add(type, priority);
    formContainer.add(row);

    // Description field
    description = new TextArea();
    description.setLabel("Description");
    description.setPlaceholder("Provide detailed information about your request...");
    description.addClassName("create-ticket-description");
    formContainer.add(description);

    // Buttons
    FlexLayout buttonLayout = FlexLayout.create()
        .horizontal()
        .justify().end()
        .build();
    buttonLayout.addClassName("button-group");

    cancelButton = new Button("Cancel");
    cancelButton.setTheme(ButtonTheme.DEFAULT);
    cancelButton.onClick(e -> handleCancel());

    submitButton = new Button("Submit Ticket");
    submitButton.setPrefixComponent(TablerIcon.create("send"));
    submitButton.setTheme(ButtonTheme.PRIMARY);
    submitButton.onClick(e -> handleSubmit());

    buttonLayout.add(cancelButton, submitButton);
    formContainer.add(buttonLayout);

    formCard.add(formContainer);
    container.add(formCard);
  }

  private void handleCancel() {
    Router.getCurrent().navigate(DashboardView.class);
  }

  private void handleSubmit() {
    // Write UI values to ticket entity and validate
    ValidationResult result = context.write(ticket);

    if (!result.isValid()) {
      // This shouldn't happen since button is disabled when form is invalid
      Toast.show("Please ensure all fields have valid entries", Theme.DANGER);
      return;
    }

    // Set the fields that aren't in the form
    User currentUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found: " + username));
    ticket.setCreatedBy(currentUser);
    ticket.setStatus(TicketStatus.OPEN);

    try {
      Ticket savedTicket = ticketService.createTicket(ticket);
      Toast.show("Ticket " + savedTicket.getTicketNumber() + " created successfully!", Theme.SUCCESS);

      // Navigate to the ticket detail view
      Router.getCurrent().navigate(TicketDetailView.class,
          ParametersBag.of("id=" + savedTicket.getId().toString()));
    } catch (Exception e) {
      Toast.show("Error creating ticket: " + e.getMessage(), Theme.DANGER);
    }
  }
}
