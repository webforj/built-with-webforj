package com.webforj.bookstore.views;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.TablerIcon;

import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.appnav.AppNav;
import com.webforj.component.layout.appnav.AppNavItem;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.annotation.Route;

/**
 * The main application layout, providing navigation and structure for the
 * application.
 * 
 * @author webforJ Bookstore
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
    self.setDrawerFooterVisible(true);
  }

  private void setHeader() {

    Toolbar toolbar = new Toolbar();
    toolbar.addToStart(TablerIcon.create("books"));
    title.setText("Bookstore");
    toolbar.addToTitle(title);

    self.addToHeader(toolbar);
  }

  private void setDrawer() {
    AppNav appNav = new AppNav();
    appNav.addItem(new AppNavItem("Inventory", InventoryView.class, TablerIcon.create("edit")));
    appNav.addItem(new AppNavItem("About", AboutView.class, TablerIcon.create("info-circle")));

    org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
        .getContext().getAuthentication();
    if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
      appNav.addItem(new AppNavItem("Management", ManagementView.class, TablerIcon.create("users")));
    }

    self.addToDrawer(appNav);
    self.addToDrawerFooter(createDrawerFooter());
  }

  private FlexLayout createDrawerFooter() {
    FlexLayout footer = new FlexLayout();
    footer.setDirection(FlexDirection.ROW);
    footer.setJustifyContent(FlexJustifyContent.END);
    Button logoutBtn = new Button();
    logoutBtn.setText("Logout");
    logoutBtn.addClickListener(e -> com.webforj.spring.security.SpringSecurityFormSubmitter.logout("/logout").submit());
    footer.add(logoutBtn);
    return footer;
  }

}
