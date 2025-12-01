package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.entity.User;
import com.webforj.builtwithwebforj.springsecurity.repository.UserRepository;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.PasswordField;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.optioninput.CheckBox;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.toast.Toast;
import com.webforj.component.dialog.Dialog;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.validation.server.ValidationResult;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

/**
 * View for creating and editing users (Admin only).
 * Handles both create mode (no id) and edit mode (with id parameter).
 */
@Route(value = "/admin/users/form/:id?", outlet = MainLayout.class)
@FrameTitle("User Form")
@RolesAllowed("ADMIN")
@StyleSheet("ws://admin-users.css")
public class CreateUserView extends Composite<Div> implements DidEnterObserver {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private final Div container = getBoundComponent();

  // Form fields - names must match User entity property names for automatic binding
  private TextField username;
  private PasswordField password;
  private TextField displayName;
  private TextField email;
  private CheckBox userRoleCheckbox;
  private CheckBox supportRoleCheckbox;
  private CheckBox adminRoleCheckbox;
  private Button submitButton;
  private Button deleteButton;
  private Button cancelButton;

  // Data binding context
  private BindingContext<User> context;
  private User user;
  private Long userId;
  private boolean isEditMode = false;

  public CreateUserView() {
    container.addClassName("create-user-view");
  }

  @Override
  public void onDidEnter(DidEnterEvent event, ParametersBag parameters) {
    parameters.get("id").ifPresentOrElse(
        id -> {
          // Edit mode - load existing user
          isEditMode = true;
          try {
            userId = Long.parseLong(id);
            user = userRepository.findById(userId).orElse(null);
            if (user == null) {
              container.add(new H1("Error: User not found"));
              return;
            }
            setupContent();
          } catch (NumberFormatException e) {
            container.add(new H1("Error: Invalid user ID"));
          }
        },
        () -> {
          // Create mode - new user
          isEditMode = false;
          userId = null;
          user = new User();
          setupContent();
        }
    );
  }

  private void setupContent() {
    container.removeAll();

    H1 title = new H1(isEditMode ? "Edit User: " + user.getUsername() : "Create New User");
    container.add(title);

    // Create form
    createForm();

    // Setup data binding
    setupDataBinding();
  }

  private void setupDataBinding() {
    // Create binding context with Jakarta validation enabled
    context = new BindingContext<>(User.class, true);

    // Manually bind only the fields that exist in User entity
    context.bind(username, "username").add();

    // Only bind password in create mode
    if (!isEditMode && password != null) {
      context.bind(password, "password").add();
    }

    context.bind(displayName, "displayName").add();
    context.bind(email, "email").add();

    // Listen for validation changes - auto enable/disable submit button
    context.addValidateListener(e -> {
      submitButton.setEnabled(e.isValid());
    });

    // Read initial values into form
    context.read(user);

    // In edit mode, load existing roles into checkboxes (roles stored without ROLE_ prefix)
    if (isEditMode) {
      userRoleCheckbox.setValue(user.getRoles().contains("USER"));
      supportRoleCheckbox.setValue(user.getRoles().contains("SUPPORT"));
      adminRoleCheckbox.setValue(user.getRoles().contains("ADMIN"));
    }
  }

  private void createForm() {
    FlexLayout formContainer = new FlexLayout();
    formContainer.setDirection(FlexDirection.COLUMN)
        .setSpacing("1.5rem");
    formContainer.addClassName("create-user-form-container");

    // Username field
    FlexLayout usernameGroup = createFormGroup("Username", "");
    username = new TextField();
    username.setPlaceholder("Enter username (lowercase, no spaces)");
    username.addClassName("create-user-field");
    usernameGroup.add(username);
    formContainer.add(usernameGroup);

    // Password field (only in create mode)
    if (!isEditMode) {
      FlexLayout passwordGroup = createFormGroup("Password", "");
      password = new PasswordField();
      password.setPlaceholder("Enter password (min 6 characters)");
      password.addClassName("create-user-field");
      passwordGroup.add(password);
      formContainer.add(passwordGroup);
    }

    // Display Name field
    FlexLayout displayNameGroup = createFormGroup("Display Name", "");
    displayName = new TextField();
    displayName.setPlaceholder("Enter full name");
    displayName.addClassName("create-user-field");
    displayNameGroup.add(displayName);
    formContainer.add(displayNameGroup);

    // Email field
    FlexLayout emailGroup = createFormGroup("Email", "(Optional)");
    email = new TextField();
    email.setPlaceholder("Enter email address");
    email.addClassName("create-user-field");
    emailGroup.add(email);
    formContainer.add(emailGroup);

    // Roles section
    FlexLayout rolesGroup = createFormGroup("Roles", "Select at least one role");
    FlexLayout rolesContainer = FlexLayout.create()
        .vertical()
        .build();
    rolesContainer.setSpacing("0.5rem");

    userRoleCheckbox = new CheckBox("USER - Can create and manage own tickets");
    userRoleCheckbox.setValue(true); // Default to USER role
    supportRoleCheckbox = new CheckBox("SUPPORT - Can view and manage all tickets");
    adminRoleCheckbox = new CheckBox("ADMIN - Full system access including user management");

    rolesContainer.add(userRoleCheckbox, supportRoleCheckbox, adminRoleCheckbox);
    rolesGroup.add(rolesContainer);
    formContainer.add(rolesGroup);

    // Buttons
    FlexLayout buttonLayout = FlexLayout.create()
        .horizontal()
        .justify().between()
        .build();

    // Delete button on left (only in edit mode)
    if (isEditMode) {
      deleteButton = new Button("Delete User");
      deleteButton.setTheme(ButtonTheme.DANGER);
      deleteButton.onClick(e -> handleDelete());
      buttonLayout.add(deleteButton);
    } else {
      // Empty div to maintain spacing when no delete button
      buttonLayout.add(new Div());
    }

    // Cancel and Submit on right
    FlexLayout rightButtons = FlexLayout.create()
        .horizontal()
        .build();
    rightButtons.setSpacing("0.5rem");

    cancelButton = new Button("Cancel");
    cancelButton.setTheme(ButtonTheme.DEFAULT);
    cancelButton.onClick(e -> handleCancel());

    submitButton = new Button(isEditMode ? "Save Changes" : "Create User");
    submitButton.setTheme(ButtonTheme.PRIMARY);
    submitButton.onClick(e -> handleSubmit());

    rightButtons.add(cancelButton, submitButton);
    buttonLayout.add(rightButtons);
    formContainer.add(buttonLayout);

    container.add(formContainer);
  }

  private FlexLayout createFormGroup(String labelText, String helpText) {
    FlexLayout group = FlexLayout.create()
        .vertical()
        .build();
    group.setSpacing("0.5rem");

    H3 label = new H3(labelText);
    label.addClassName("form-group-label");
    group.add(label);

    if (!helpText.isEmpty()) {
      Paragraph help = new Paragraph(helpText);
      help.addClassName("form-group-help");
      group.add(help);
    }

    return group;
  }

  private void handleCancel() {
    Router.getCurrent().navigate(AdminUsersView.class);
  }

  private void handleSubmit() {
    // Write UI values to user entity and validate
    ValidationResult result = context.write(user);

    if (!result.isValid()) {
      Toast.show("Please ensure all fields have valid entries", Theme.DANGER);
      return;
    }

    // Collect selected roles (store without ROLE_ prefix - CustomUserDetailsService adds it)
    HashSet<String> selectedRoles = new HashSet<>();
    if (userRoleCheckbox.getValue()) {
      selectedRoles.add("USER");
    }
    if (supportRoleCheckbox.getValue()) {
      selectedRoles.add("SUPPORT");
    }
    if (adminRoleCheckbox.getValue()) {
      selectedRoles.add("ADMIN");
    }

    if (selectedRoles.isEmpty()) {
      Toast.show("Please select at least one role", Theme.DANGER);
      return;
    }

    user.setRoles(selectedRoles);

    try {
      if (isEditMode) {
        // Update existing user (password remains unchanged)
        // Check if username changed and if new username already exists
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null && !existingUser.getUsername().equals(user.getUsername())) {
          if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            Toast.show("Username already exists. Please choose a different username.", Theme.DANGER);
            return;
          }
        }

        userRepository.save(user);
        Toast.show("User updated successfully!", Theme.SUCCESS);
      } else {
        // Create new user
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
          Toast.show("Username already exists. Please choose a different username.", Theme.DANGER);
          return;
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        Toast.show("User " + user.getUsername() + " created successfully!", Theme.SUCCESS);
      }

      // Navigate back to users list
      Router.getCurrent().navigate(AdminUsersView.class);
    } catch (Exception e) {
      Toast.show("Error saving user: " + e.getMessage(), Theme.DANGER);
    }
  }

  private void handleDelete() {
    // Show confirmation dialog
    Dialog confirmDialog = new Dialog();

    Div content = new Div();
    Paragraph message = new Paragraph("Are you sure you want to delete user '" + user.getUsername() + "'? This action cannot be undone.");
    content.add(message);
    confirmDialog.add(content);

    Button confirmButton = new Button("Delete");
    confirmButton.setTheme(ButtonTheme.DANGER);
    confirmButton.onClick(e -> {
      try {
        userRepository.delete(user);
        confirmDialog.close();
        Toast.show("User deleted successfully", Theme.SUCCESS);
        Router.getCurrent().navigate(AdminUsersView.class);
      } catch (Exception ex) {
        Toast.show("Error deleting user: " + ex.getMessage(), Theme.DANGER);
      }
    });

    Button cancelButton = new Button("Cancel");
    cancelButton.setTheme(ButtonTheme.DEFAULT);
    cancelButton.onClick(e -> confirmDialog.close());

    confirmDialog.addToFooter(cancelButton, confirmButton);
    confirmDialog.open();
  }
}
