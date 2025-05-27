package com.webforj.howdy.views;

import java.util.Set;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.applayout.AppLayout.DrawerPlacement;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.tabbedpane.Tab;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.component.tabbedpane.event.TabSelectEvent;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.event.NavigateEvent;
import com.webforj.router.history.Location;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;

/**
 * MainLayout serves as the primary layout for the application. It extends the {@code Composite<AppLayout>}
 * and implements the {@code DidEnterObserver} interface, meaning it observes and reacts to navigation events.
 *
 * The layout is comprised of the following key components:
 * - A dynamic header that updates the application title based on the active view.
 * - A bottom navigation bar that allows quick access to different sections of the application.
 *
 * Functionality Overview:
 * - Listens for navigation events to update the current view, title, and selected tab accordingly.
 * - Supports dynamic tab selection based on user interactions and navigation paths.
 * - Prevents history stack pollution during programmatic tab selections by temporarily unregistering tab selection listeners.
 *
 * Key methods include:
 * - {@code setHeader()}: Configures the application header.
 * - {@code setNav()}: Initializes the navigation bar.
 * - {@code onNavigate(NavigateEvent ev)}: Handles application navigation events to update the layout.
 * - {@code setAppTitle(NavigateEvent ev)}: Updates the header title based on the active view's {@code FrameTitle} annotation.
 * - {@code setSelectedTab(NavigateEvent ev)}: Selects the appropriate navigation tab based on the current path.
 * - {@code setSelectListener()}: Registers a listener for tab selection changes to handle user interactions.
 * - {@code removeSelectListener()}: Deregisters the tab selection listener to temporarily avoid unwanted interaction during programmatic actions.
 */
@Route
public class MainLayout extends Composite<AppLayout> implements DidEnterObserver {
  private static final String DEFAULT_VIEW = "dashboard";
  private AppLayout self = getBoundComponent();
  private TabbedPane nav = new TabbedPane();
  private H1 title = new H1();
  private ListenerRegistration<TabSelectEvent> registration;

  public MainLayout() {
    setHeader();
    setNav();
    Router.getCurrent().onNavigate(this::onNavigate);
  }

  /**
   * Handles the "did enter" event for the main layout. This method is triggered
   * when navigation enters the associated route of the layout. It sets up a
   * selection listener on the navigation component to handle navigation events.
   *
   * @param event The event object representing the "did enter" event, which
   *              provides details about the navigation context.
   * @param parameters A collection of parameters associated with the "did enter"
   *                   event or the navigation context.
   */
  @Override
  public void onDidEnter(DidEnterEvent event, ParametersBag parameters) {
    setSelectListener();
  }

  /**
   * Configures and sets up the header structure of the main layout.
   *
   * This method performs the following actions:
   * - Hides the drawer by setting its placement to `DrawerPlacement.HIDDEN`.
   * - Creates a new instance of the `Toolbar` component.
   * - Adds a header title to the toolbar, including the text "Howdy App - " followed by the current value of the `title` field.
   * - Adds the configured toolbar to the header of the main layout.
   */
  private void setHeader() {
    self.setDrawerPlacement(DrawerPlacement.HIDDEN);

    Toolbar toolbar = new Toolbar();
    toolbar.addToTitle(new H1("Howdy App - "));
    toolbar.addToTitle(title);

    self.addToHeader(toolbar);
  }

  /**
   * Configures and initializes the application's navigation bar.
   *
   * This method sets up the navigation component with specific properties and adds tabs
   * corresponding to different application views. The configuration includes:
   * - Adding a CSS class name for styling.
   * - Setting the body to be hidden.
   * - Making the navigation bar borderless.
   * - Placing the tabs at the bottom of the navigation bar.
   * - Center-aligning the tabs within the navigation bar.
   *
   * The method also creates and adds multiple tabs to the navigation bar:
   * - "You" tab, represented with an associated "user" icon.
   * - "Dashboard" tab, represented with an associated "dashboard" icon.
   * - "Users" tab, represented with an associated "users" icon.
   *
   * Once configured, the navigation bar is added to the footer of the parent component.
   */
  private void setNav() {
    nav.addClassName("app-nav");
    nav.setBodyHidden(true);
    nav.setBorderless(true);
    nav.setPlacement(TabbedPane.Placement.BOTTOM);
    nav.setAlignment(TabbedPane.Alignment.CENTER);

    nav.addTab(new Tab("You", TablerIcon.create("user")));
    nav.addTab(new Tab("Dashboard", TablerIcon.create("dashboard")));
    nav.addTab(new Tab("Users", TablerIcon.create("users")));


    self.addToFooter(nav);
  }

  /**
   * Handles navigation events within the main layout.
   *
   * This method updates the application's title and selects the appropriate
   * navigation tab based on the current navigation event.
   *
   * @param ev The navigation event object that contains the context and
   *           details about the current navigation action.
   */
  private void onNavigate(NavigateEvent ev) {
    setAppTitle(ev);
    setSelectedTab(ev);
  }

  /**
   * Sets the application title based on the current navigation event and the associated view.
   *
   * This method retrieves the current view component from the navigation event context and
   * checks if it is annotated with the {@code FrameTitle} annotation. If the annotation exists,
   * the application's title is set to the value specified in the annotation. If the annotation
   * is not present, the title is set to an empty string.
   *
   * @param ev The navigation event containing the details of the current navigation context,
   *           including the components associated with the event.
   */
  private void setAppTitle(NavigateEvent ev) {
    Set<Component> components = ev.getContext().getAllComponents();
    Component view = components.stream().filter(c -> c.getClass().getSimpleName().endsWith("View")).findFirst()
        .orElse(null);

    if (view != null) {
      FrameTitle frameTitle = view.getClass().getAnnotation(FrameTitle.class);
      title.setText(frameTitle != null ? frameTitle.value() : "");
    }
  }

  /**
   * Updates the selected tab in the navigation bar based on the current navigation event.
   *
   * This method determines the correct tab to select by extracting the path from the
   * provided NavigateEvent. If the path is empty, it defaults to the predefined default view.
   * It then iterates through the available tabs and selects the one matching the path,
   * ensuring that the selection does not trigger additional navigation events by temporarily
   * removing and re-adding the select listener.
   *
   * @param ev The navigation event containing details about the current navigation context,
   *           including the full URI used to determine the appropriate tab.
   */
  private void setSelectedTab(NavigateEvent ev) {
    String path = ev.getLocation().getFullURI().substring(1);
    if (path.isEmpty()) {
      path = DEFAULT_VIEW;
    }

    for (Tab tab : nav.getTabs()) {
      if (tab.getText().toLowerCase().equals(path)) {
        // temporarily remove the listener to avoid history push
        removeSelectListener();
        nav.select(tab);
        setSelectListener();
        break;
      }
    }
  }

  /**
   * Configures and attaches a selection listener to the navigation component.
   *
   * This method sets up an event listener on the `nav` component to handle tab selection events.
   * When a tab is selected, the listener retrieves the text associated with the selected tab,
   * converts it to lowercase, and navigates to the corresponding route using the application's
   * router.
   *
   * The method assigns the listener's registration to the `registration` field to enable
   * proper management of the listener's lifecycle and potential removal.
   */
  private void setSelectListener() {
    registration = nav.onSelect(ev -> {
      String tab = ev.getTab().getText().toLowerCase();
      Router.getCurrent().navigate(new Location(tab));
    });
  }

  /**
   * Removes the previously registered selection listener.
   *
   * This method checks if a selection listener registration exists and removes it,
   * ensuring proper cleanup of listener resources. This is particularly important
   * to prevent memory leaks and unintended execution of the listener after it is no longer needed.
   */
  private void removeSelectListener() {
    if (registration != null) {
      registration.remove();
    }
  }
}
