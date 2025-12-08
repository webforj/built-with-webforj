package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.components.CommentCard;
import com.webforj.builtwithwebforj.springsecurity.components.TicketMetadataCard;
import com.webforj.builtwithwebforj.springsecurity.entity.Comment;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.Ticket;
import com.webforj.builtwithwebforj.springsecurity.entity.ticket.TicketStatus;
import com.webforj.builtwithwebforj.springsecurity.service.CommentService;
import com.webforj.builtwithwebforj.springsecurity.service.TicketService;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextArea;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.toast.Toast;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * View for displaying ticket details.
 *
 * Access model:
 * - Must be logged in (any authenticated user with USER, SUPPORT, or ADMIN role)
 * - Any logged-in user can view any ticket and add comments
 * - Only ticket owner, SUPPORT, or ADMIN can change ticket status
 * - SUPPORT/ADMIN can see internal notes; regular users cannot
 */
@Route(value = "/tickets/:id", outlet = MainLayout.class)
@FrameTitle("Ticket Detail")
@RolesAllowed({"USER", "SUPPORT", "ADMIN"})
@StyleSheet("ws://ticket-detail.css")
public class TicketDetailView extends Composite<Div> implements DidEnterObserver {

  @Autowired
  private TicketService ticketService;

  @Autowired
  private CommentService commentService;

  private final Div container = getBoundComponent();
  private String username;
  private boolean isSupport;
  private boolean isAdmin;
  private Long ticketId;
  private Ticket ticket;
  private Div commentsContainer;
  private TextArea commentTextArea;

  public TicketDetailView() {
    container.addClassName("view-container");
  }

  @Override
  public void onDidEnter(DidEnterEvent event, ParametersBag params) {
    // Get ticket ID from route parameters
    String ticketIdStr = params.get("id").orElse(null);
    if (ticketIdStr == null) {
      container.add(new H1("Error: Ticket ID not found"));
      return;
    }

    try {
      ticketId = Long.parseLong(ticketIdStr);
    } catch (NumberFormatException e) {
      container.add(new H1("Error: Invalid ticket ID"));
      return;
    }

    // Get current user and roles
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated()) {
      username = auth.getName();
      isSupport = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_SUPPORT"));
      isAdmin = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    // Load ticket
    try {
      ticket = ticketService.getTicketById(ticketId);
      setupContent();
    } catch (RuntimeException e) {
      container.add(new H1("Error: Ticket not found"));
      container.add(new Paragraph(e.getMessage()));
    }
  }

  private void setupContent() {
    container.removeAll();

    // Back button
    Button backButton = new Button("Back");
    backButton.setPrefixComponent(TablerIcon.create("arrow-left"));
    backButton.setTheme(ButtonTheme.DEFAULT);
    backButton.addClassName("back-button");
    backButton.onClick(e -> Router.getCurrent().navigate(DashboardView.class));
    container.add(backButton);

    // Page header
    FlexLayout header = FlexLayout.create()
        .horizontal()
        .justify().between()
        .align().start()
        .build();
    header.addClassName("page-header");

    // Title section
    Div titleSection = new Div();

    Span ticketNumber = new Span(ticket.getTicketNumber());
    ticketNumber.addClassName("ticket-number");
    titleSection.add(ticketNumber);

    H1 title = new H1(ticket.getSubject());
    title.addClassName("page-title");
    titleSection.add(title);

    header.add(titleSection);

    // Status dropdown (for owner, support, or admin)
    boolean isOwner = ticket.getCreatedBy().getUsername().equals(username);
    if (isOwner || isSupport || isAdmin) {
      ChoiceBox statusDropdown = new ChoiceBox();
      statusDropdown.setLabel("Status");
      statusDropdown.setWidth("180px");

      for (TicketStatus status : TicketStatus.values()) {
        statusDropdown.add(status, status.name().replace("_", " "));
      }
      statusDropdown.selectKey(ticket.getStatus());
      statusDropdown.onSelect(e -> {
        TicketStatus newStatus = (TicketStatus) e.getSelectedItem().getKey();
        handleStatusChange(newStatus);
      });

      header.add(statusDropdown);
    }

    container.add(header);

    // Metadata card
    container.add(new TicketMetadataCard(ticket));

    // Description
    container.add(createDescriptionSection());

    // Comments
    container.add(createCommentsSection());

    // Add comment form (if not closed)
    if (!ticket.getStatus().equals(TicketStatus.CLOSED)) {
      container.add(createAddCommentForm());
    }
  }

  private Div createDescriptionSection() {
    Div section = new Div();
    section.addClassName("section");

    H2 heading = new H2("Description");
    heading.addClassName("section-title");

    Div descriptionBox = new Div();
    descriptionBox.addClassName("card");

    Paragraph description = new Paragraph(ticket.getDescription());
    description.addClassName("description-text");
    descriptionBox.add(description);

    section.add(heading, descriptionBox);
    return section;
  }

  private Div createCommentsSection() {
    Div section = new Div();
    section.addClassName("section");

    H2 heading = new H2("Comments");
    heading.addClassName("section-title");

    commentsContainer = new Div();
    commentsContainer.addClassName("comments-list");

    loadComments();

    section.add(heading, commentsContainer);
    return section;
  }

  private void loadComments() {
    commentsContainer.removeAll();

    List<Comment> comments;
    if (isSupport || isAdmin) {
      comments = commentService.getAllComments(ticketId);
    } else {
      comments = commentService.getPublicComments(ticketId);
    }

    if (comments.isEmpty()) {
      Paragraph noComments = new Paragraph("No comments yet.");
      noComments.addClassName("no-comments");
      commentsContainer.add(noComments);
      return;
    }

    for (Comment comment : comments) {
      commentsContainer.add(new CommentCard(comment));
    }
  }

  private Div createAddCommentForm() {
    Div section = new Div();
    section.addClassName("section");

    H2 heading = new H2("Add Comment");
    heading.addClassName("section-title");

    Div formCard = new Div();
    formCard.addClassName("card");

    commentTextArea = new TextArea();
    commentTextArea.setPlaceholder("Write your comment here...");
    commentTextArea.addClassName("comment-textarea");

    FlexLayout buttonLayout = FlexLayout.create()
        .horizontal()
        .justify().end()
        .build();
    buttonLayout.addClassName("button-group");

    if (isSupport || isAdmin) {
      Button internalNoteButton = new Button("Internal Note");
      internalNoteButton.setPrefixComponent(TablerIcon.create("lock"));
      internalNoteButton.setTheme(ButtonTheme.DEFAULT);
      internalNoteButton.onClick(e -> handleAddComment(true));

      Button publicCommentButton = new Button("Post Comment");
      publicCommentButton.setPrefixComponent(TablerIcon.create("send"));
      publicCommentButton.setTheme(ButtonTheme.PRIMARY);
      publicCommentButton.onClick(e -> handleAddComment(false));

      buttonLayout.add(internalNoteButton, publicCommentButton);
    } else {
      Button addCommentButton = new Button("Post Comment");
      addCommentButton.setPrefixComponent(TablerIcon.create("send"));
      addCommentButton.setTheme(ButtonTheme.PRIMARY);
      addCommentButton.onClick(e -> handleAddComment(false));

      buttonLayout.add(addCommentButton);
    }

    formCard.add(commentTextArea, buttonLayout);
    section.add(heading, formCard);
    return section;
  }

  private void handleAddComment(boolean isInternal) {
    String text = commentTextArea.getValue();
    if (text == null || text.trim().isEmpty()) {
      Toast.show("Please enter a comment", Theme.DANGER);
      return;
    }

    if (text.trim().length() > 2000) {
      Toast.show("Comment must be 2000 characters or less", Theme.DANGER);
      return;
    }

    try {
      commentService.addComment(ticketId, text.trim(), username, isInternal);
      commentTextArea.setValue("");
      loadComments();

      if (isInternal) {
        Toast.show("Internal note added successfully", Theme.SUCCESS);
      } else {
        Toast.show("Comment added successfully", Theme.SUCCESS);
      }
    } catch (Exception e) {
      Toast.show("Error adding comment: " + e.getMessage(), Theme.DANGER);
    }
  }

  private void handleStatusChange(TicketStatus newStatus) {
    // Don't do anything if status hasn't changed
    if (newStatus.equals(ticket.getStatus())) {
      return;
    }

    try {
      ticketService.updateTicketStatus(ticketId, newStatus);
      Toast.show("Ticket status updated to " + newStatus.name(), Theme.SUCCESS);

      // Refresh the view
      ticket = ticketService.getTicketById(ticketId);
      setupContent();
    } catch (Exception e) {
      Toast.show("Error updating ticket status: " + e.getMessage(), Theme.DANGER);
    }
  }
}
