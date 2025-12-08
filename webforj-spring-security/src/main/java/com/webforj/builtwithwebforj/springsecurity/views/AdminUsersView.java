package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.renderers.user.EmailLinkRenderer;
import com.webforj.builtwithwebforj.springsecurity.renderers.user.RolesChipRenderer;
import com.webforj.builtwithwebforj.springsecurity.renderers.user.UserAvatarRenderer;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.table.Table;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.ParametersBag;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin view for managing users.
 * Displays all users in the system with their roles.
 */
@Route(value = "/admin/users", outlet = MainLayout.class)
@FrameTitle("User Management")
@RolesAllowed("ADMIN")
@StyleSheet("ws://admin-users.css")
public class AdminUsersView extends Composite<Div> {

  @Autowired
  private UserRepository userRepository;

  private final Div container = getBoundComponent();

  public AdminUsersView() {
    // Constructor - dependencies not yet injected
  }

  @PostConstruct
  private void init() {
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
    H1 title = new H1("User Management");
    title.addClassName("page-title");

    Paragraph subtitle = new Paragraph("Manage system users and their roles");
    subtitle.addClassName("page-subtitle");

    titleSection.add(title, subtitle);
    header.add(titleSection);

    Button createButton = new Button("New User");
    createButton.setPrefixComponent(TablerIcon.create("user-plus"));
    createButton.setTheme(ButtonTheme.PRIMARY);
    createButton.onClick(e -> Router.getCurrent().navigate(CreateUserView.class));
    header.add(createButton);

    container.add(header);

    // Load and display users table
    loadUsers();
  }

  private void loadUsers() {
    List<User> users = userRepository.findAll();

    // Create table
    Table<User> table = new Table<>();
    table.addClassName("users-table");

    // Hidden columns for renderer data access
    table.addColumn("displayName", User::getDisplayName).setHidden(true);

    // Username column with avatar
    table.addColumn("username", User::getUsername)
        .setLabel("User")
        .setRenderer(new UserAvatarRenderer());

    // Email column with mailto: link
    table.addColumn("email", User::getEmail)
        .setLabel("Email")
        .setRenderer(new EmailLinkRenderer());

    // Roles column with chips
    table.addColumn("roles", user -> user.getRoles().stream()
        .map(role -> role.replace("ROLE_", ""))
        .collect(Collectors.joining(", ")))
        .setLabel("Roles")
        .setRenderer(new RolesChipRenderer());

    // Set table data
    table.setItems(users);
    table.setColumnsToAutoFit();

    // Handle row clicks to navigate to edit view
    table.onItemClick(event -> {
      User clickedUser = event.getItem();
      Router.getCurrent().navigate(CreateUserView.class,
          ParametersBag.of("id=" + clickedUser.getId().toString()));
    });

    container.add(table);
  }
}
