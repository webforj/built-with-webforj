package com.webforj.builtwithwebforj.springsecurity.views;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.webforj.builtwithwebforj.springsecurity.components.DrawerHeader;
import com.webforj.builtwithwebforj.springsecurity.components.UserInfo;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.appnav.AppNav;
import com.webforj.component.layout.appnav.AppNavItem;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;
import com.webforj.spring.security.SpringSecurityFormSubmitter;

import jakarta.annotation.security.PermitAll;

@Route
@PermitAll
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private H1 title = new H1();

  public MainLayout() {
    setHeader();
    setDrawer();
    Router.getCurrent().onNavigate(this::onNavigate);
  }

  private void setHeader() {
    // Hide the drawer completely
    self.setDrawerPlacement(AppLayout.DrawerPlacement.HIDDEN);

    // Make header full width (not shifted by drawer)
    self.setHeaderOffscreen(false);

    Toolbar toolbar = new Toolbar();
    toolbar.addToTitle(title);

    UserInfo userInfo = new UserInfo();
    toolbar.addToEnd(userInfo);

    Button logout = new Button("LOGOUT");
    logout.onClick(e -> SpringSecurityFormSubmitter.logout("/logout").submit());
    toolbar.addToEnd(logout);
    
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = auth != null && auth.getAuthorities().stream()
      .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    // Conditionally add admin-only button
    if (isAdmin) {
      Button adminPanel = new Button("Admin Panel");
      toolbar.addToEnd(adminPanel);
    }

    // Could this instead be a setItems with a controller.getTicketsByUser() and a controller.getTickets()
    

    self.addToHeader(toolbar);
  }

  private void setDrawer() {
    // Drawer is hidden, so no need to set up drawer content
    // This method can be removed or kept for future use
  }

  private void onNavigate(NavigateEvent ev) {
    Set<Component> components = ev.getContext().getAllComponents();
    Component view = components.stream().filter(c -> c.getClass().getSimpleName().endsWith("View")).findFirst()
        .orElse(null);

    if (view != null) {
      FrameTitle frameTitle = view.getClass().getAnnotation(FrameTitle.class);
      title.setText(frameTitle != null ? frameTitle.value() : "");
    }
  }
}
