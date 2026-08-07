package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.builtwithwebforj.springsecurity.components.PageHeader;
import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.service.UserService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.table.Table;
import com.webforj.component.table.renderer.AvatarRenderer;
import com.webforj.builtwithwebforj.springsecurity.renderers.RoleBadgesRenderer;
import com.webforj.component.table.renderer.CompositeRenderer;
import com.webforj.component.table.renderer.EmailRenderer;
import com.webforj.component.table.renderer.TextRenderer;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.ParametersBag;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin view for managing users.
 * Displays all users in the system with their roles.
 */
@Route(value = "/admin/users", outlet = MainLayout.class)
@FrameTitle("User Management")
@RolesAllowed("ADMIN")
public class AdminUsersView extends Composite<Div> {

  @Autowired
  private UserService userService;

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

    // Page header with action button
    Button createButton = new Button("New User");
    createButton.setPrefixComponent(TablerIcon.create("user-plus"));
    createButton.setTheme(ButtonTheme.PRIMARY);
    createButton.onClick(e -> Router.getCurrent().navigate(CreateUserView.class));

    PageHeader header = new PageHeader(
        "User Management",
        "Manage system users and their roles",
        createButton
    );
    container.add(header);

    // Load and display users table
    loadUsers();
  }

  private void loadUsers() {
    List<User> users = userService.getAllUsers();

    // Create table
    Table<User> table = new Table<>();
    table.setHeight("calc(90dvh - 260px)");
    table.setRowHeight(76);
    table.setHeaderHeight(44);
    table.setStriped(true);
    table.setBordersVisible(EnumSet.of(Table.Border.ROWS));

    // User column — avatar + display name
    table.addColumn("username", User::getDisplayName)
        .setLabel("User")
        .setFlex(1.5f)
        .setRenderer(new CompositeRenderer<>(
            new AvatarRenderer<>(),
            new TextRenderer<>()));

    // Email column with mailto: link
    table.addColumn("email", User::getEmail)
        .setLabel("Email")
        .setFlex(2f)
        .setRenderer(new EmailRenderer<>());

    // Roles column — multiple colored badges
    table.addColumn("roles", user -> user.getRoles().stream()
        .map(role -> role.replace("ROLE_", ""))
        .collect(Collectors.joining(", ")))
        .setLabel("Roles")
        .setFlex(1f)
        .setRenderer(new RoleBadgesRenderer<>());

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
