package com.webforj.howdy.views;

import com.webforj.component.table.Table;
import com.webforj.data.repository.CollectionRepository;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.environment.namespace.PrivateNamespace;
import com.webforj.environment.namespace.event.NamespaceChangeEvent;


import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.howdy.components.NoData;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.List;

/**
 * The UsersView class represents a view in the application displaying user mood data
 * in a table format. This class extends a Composite and utilizes the FlexLayout
 * as its main layout container. It also integrates with a PrivateNamespace model
 * to dynamically retrieve and update data.
 *
 * This class is annotated with @Route to define the navigation route and @FrameTitle
 * to specify the title displayed in the application's frame.
 *
 * Key functionality includes:
 * - Integration with a PrivateNamespace model to manage data dynamically.
 * - Automatic update of the table when the underlying namespace data changes.
 * - Display of user mood data in a table with columns for "user" and corresponding "mood".
 *
 * Constructor:
 * - Sets up the FlexLayout with full height and center alignment.
 * - Initializes the UserTable by defining its columns and integrating it with
 *   the PrivateNamespace model.
 * - Binds data updates to the `updateData` method triggered upon changes in the model.
 *
 * Methods:
 * - updateData(NamespaceChangeEvent namespaceChangeEvent): Updates the table data
 *   with the latest user mood information retrieved from the PrivateNamespace model.
 */
@Route(value = "/users", outlet = MainLayout.class)
@FrameTitle("Users")
public class UsersView extends Composite<FlexLayout> {

  private final ListenerRegistration<NamespaceChangeEvent> eventListenerReg;
  /**
   * Represents the current instance of the layout bound to the view.
   *
   * This variable is an instance of `FlexLayout` and serves as the root layout
   * for the `UsersView` class. It is initialized by invoking the `getBoundComponent`
   * method, which links the layout component to the view. The `self` variable
   * is used to configure the overall layout of the view, including its alignment,
   * dimensions, and the addition of child components.
   */
  private FlexLayout self = getBoundComponent();

  /**
   * Represents a `PrivateNamespace` instance used for managing and storing
   * application-specific data related to the "HowdyApp" in the "Board" namespace.
   *
   * This variable serves as a centralized data model for storing and retrieving
   * user-specific information (like user moods) within the application. It is
   * scoped to the "HowdyApp" application and ensures data isolation with locking
   * mechanisms to prevent concurrent modifications.
   *
   * Key responsibilities:
   * - Facilitates secure data storage for the "HowdyApp".
   * - Provides mechanisms to interact with and manage namespace-specific data.
   * - Ensures thread-safe operations in a multi-user environment through locking.
   *
   * The `true` parameter specifies that the namespace is private, ensuring that
   * data within this namespace is isolated from other namespaces in the application.
   */
  PrivateNamespace model = new PrivateNamespace("HowdyApp", "Board", true);

  /**
   * Represents the mood of a user.
   *
   * The `UserMood` record pairs a user identifier (user) with their current mood.
   * This class is used to encapsulate user mood information, typically for display
   * or data processing purposes.
   *
   * Immutable by design, each `UserMood` instance holds a snapshot of the user and
   * mood association at the time of creation.
   *
   * Fields:
   * - `user`: The unique identifier or name of the user.
   * - `mood`: The current mood associated with the user.
   */
  record UserMood(String user, String mood) {}

  /**
   * Represents a table component in the `UsersView` class that is used to display user mood data
   * in a tabular format.
   *
   * The table holds data of type `UserMood`, where each record contains user information, including
   * their name and mood. It is initialized and managed within the `UsersView` class, and its data
   * is dynamically updated based on changes in the associated `PrivateNamespace` model.
   *
   * Key responsibilities of the `UserTable` include:
   * - Displaying rows of user mood data with columns for user name and mood information.
   * - Binding data changes from the `PrivateNamespace` model to automatically reflect updates
   *   in the displayed data.
   * - Supporting the addition and updating of records for user mood tracking.
   */
  Table <UserMood> userTable = new Table<>();
  NoData noData = new NoData();

  /**
   * Constructs a new instance of the UsersView class.
   *
   * This constructor initializes the view layout and sets up the `UserTable` component
   * to display user mood information in a tabular format. The layout is configured with
   * a full height and center alignment. Two columns, "user" and "mood", are added to
   * the table for displaying user-related data.
   *
   * The class binds the table's data to the `PrivateNamespace` model and registers a
   * listener to automatically update the data whenever changes occur in the model.
   * Upon instantiation, it immediately populates the table with the current data in
   * the model by invoking the `updateData` method.
   */
  public UsersView() {
    self.setHeight("100%");
    self.setAlignment(FlexAlignment.CENTER);
    userTable.addColumn("user", UserMood::user);
    userTable.addColumn("mood", UserMood::mood);
    userTable.setVisible(false);
    self.add(userTable, noData);
    this.eventListenerReg = model.onChange(this::updateData);
    updateData(null);
  }

  /**
   * Finalizes the destruction of the view and ensures cleanup of resources.
   *
   * This method is called during the teardown of the `DashboardView` component
   * to perform custom destruction logic before the view is fully destroyed.
   * It removes the registered event listener to prevent memory leaks or unintended
   * event processing after the view is no longer active.
   *
   * The base class's `onDidDestroy` method is invoked first to ensure any generic
   * destruction handled by the superclass is performed.
   */
  @Override
  protected void onDidDestroy() {
    super.onDidDestroy();
    this.eventListenerReg.remove();
  }


  /**
   * Updates the data displayed in the `UserTable` with the latest user mood information
   * retrieved from the `PrivateNamespace` model.
   *
   * The method converts the data from the model into a list of `UserMood` objects,
   * updates the data repository of the table with the new list, and commits the
   * changes to ensure the table reflects the latest data.
   *
   * @param namespaceChangeEvent the event triggered when the namespace changes.
   *                             This can be used to identify and respond to changes
   *                             in the underlying model. It can be null if no specific
   *                             event is associated with the update.
   */
  private void updateData(NamespaceChangeEvent namespaceChangeEvent) {
    List<UserMood> data = model.keySet().stream()
      .map(k -> new UserMood(k, model.get(k).toString()))
      .toList();

    CollectionRepository<UserMood> dataRepository = new CollectionRepository<>(data);
    userTable.setRepository(dataRepository);
    dataRepository.commit();

    noData.setVisible(data.isEmpty());
    userTable.setVisible(!data.isEmpty());

  }
}
