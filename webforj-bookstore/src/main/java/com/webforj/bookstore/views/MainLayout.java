package com.webforj.bookstore.views;

import com.webforj.App;
import com.webforj.component.Composite;
import com.webforj.component.avatar.Avatar;
import com.webforj.component.button.Button;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Span;
import com.webforj.component.html.elements.Img;
import com.webforj.component.icons.TablerIcon;

import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.appnav.AppNav;
import com.webforj.component.layout.appnav.AppNavItem;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.annotation.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The main application layout, providing navigation and structure for the
 * application.
 * 
 */
@Route
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private H1 title = new H1();

  /**
   * Constructs the MainLayout and initializes the header and drawer.
   */
  public MainLayout() {
    setHeader();
    setDrawer();
    self.setDrawerOpened(true);
    self.setDrawerPlacement(AppLayout.DrawerPlacement.LEFT);
    self.setHeaderOffscreen(false);
    self.setDrawerHeaderVisible(false);
  }

  private void setHeader() {

    Toolbar toolbar = new Toolbar();
    toolbar.addToStart(TablerIcon.create("books"));
    title.setText("Bookstore");
    toolbar.addToTitle(title);

    Authentication auth = SecurityContextHolder
        .getContext().getAuthentication();
    String username = auth != null ? auth.getName() : "User";

    FlexLayout userProfile = createUserProfile(username);
    toolbar.addToEnd(userProfile);

    // Create container for separator and logout button
    FlexLayout logoutContainer = FlexLayout.create()
        .horizontal()
        .align().center()
        .build();

    // Add separator
    Div separator = new Div();
    separator.setStyle("width", "1px");
    separator.setStyle("height", "var(--dwc-size-l)");
    separator.setStyle("border-left", "1px solid var(--dwc-color-default)");
    separator.setStyle("margin", "0 var(--dwc-space-m)");
    logoutContainer.add(separator);

    Button logoutBtn = new Button();
    logoutBtn.setText("Logout");
    logoutBtn.setPrefixComponent(TablerIcon.create("logout"));
    logoutBtn.addClickListener(e -> com.webforj.spring.security.SpringSecurityFormSubmitter.logout("/logout").submit());
    logoutContainer.add(logoutBtn);

    toolbar.addToEnd(logoutContainer);

    self.addToHeader(toolbar);
  }

  private void setDrawer() {
    AppNav appNav = new AppNav();
    appNav.addItem(new AppNavItem("Inventory", InventoryView.class, TablerIcon.create("edit")));
    appNav.addItem(new AppNavItem(App.getApplicationName(), AboutView.class, TablerIcon.create("info-circle")));

    Authentication auth = SecurityContextHolder
        .getContext().getAuthentication();
    if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      appNav.addItem(new AppNavItem("Management", ManagementView.class, TablerIcon.create("users")));
    }

    self.addToDrawer(appNav);
  }

  private FlexLayout createUserProfile(String username) {
    Avatar avatar = new Avatar(username, new Img("ws://avatar.png"));

    Span usernameLabel = new Span(username);

    return FlexLayout.create(avatar, usernameLabel)
        .horizontal()
        .align().center()
        .build()
        .setSpacing("var(--dwc-space-s)");
  }

}
