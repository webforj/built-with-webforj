package com.webforj.builtwithwebforj.springsecurity.views;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.webforj.App;
import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.components.UserInfo;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.tabbedpane.Tab;
import com.webforj.component.tabbedpane.TabbedPane;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.spring.security.SpringSecurityFormSubmitter;

import jakarta.annotation.security.PermitAll;

@Route
@PermitAll
@StyleSheet("ws://main-layout.css")
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private boolean isDarkTheme = false;
  private IconButton themeToggle;

  public MainLayout() {
    isDarkTheme = "dark".equals(App.getTheme());
    self.setDrawerPlacement(AppLayout.DrawerPlacement.HIDDEN);
    self.setHeaderOffscreen(false);
    setupHeader();
  }

  private void setupHeader() {
    Toolbar toolbar = new Toolbar();
    toolbar.addClassName("app-toolbar");

    // ─── LEFT: Brand ───
    Span brandText = new Span("Support Desk");
    brandText.addClassName("brand-text");
    FlexLayout brand = FlexLayout.create(
        TablerIcon.create("ticket"),
        brandText
    ).horizontal().align().center().build();
    brand.addClassName("brand");
    brand.setSpacing("var(--dwc-space-xs)");
    brand.onClick(e -> Router.getCurrent().navigate(DashboardView.class));
    toolbar.addToStart(brand);

    // ─── CENTER: Navigation (admin only) ───
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = auth != null && auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    if (isAdmin) {
      TabbedPane nav = new TabbedPane();
      nav.addClassName("nav-tabs");
      nav.setSegment(true);
      nav.setBorderless(true);
      nav.setTheme(Theme.PRIMARY);

      nav.addTab(new Tab("Dashboard", TablerIcon.create("layout-dashboard")));
      nav.addTab(new Tab("Users", TablerIcon.create("users")));

      nav.onSelect(event -> {
        int idx = event.getTabIndex();
        if (idx == 0) {
          Router.getCurrent().navigate(DashboardView.class);
        } else if (idx == 1) {
          Router.getCurrent().navigate(AdminUsersView.class);
        }
      });

      // Keep tab selection in sync with the active route
      Router.getCurrent().onNavigate(navEvent -> {
        String path = navEvent.getLocation().getFullURI();
        nav.select(path.startsWith("/admin/users") ? 1 : 0);
      });

      toolbar.addToTitle(nav);
    }

    // ─── RIGHT: Actions ───
    FlexLayout actions = FlexLayout.create().horizontal().align().center().build();
    actions.addClassName("toolbar-actions");
    actions.setSpacing("var(--dwc-space-xs)");

    // User chip
    UserInfo userInfo = new UserInfo();
    actions.add(userInfo);

    // Divider
    Span sep = new Span();
    sep.addClassName("toolbar-divider");
    actions.add(sep);

    // Theme toggle
    themeToggle = new IconButton(TablerIcon.create(isDarkTheme ? "moon" : "sun"));
    themeToggle.addClassName("toolbar-icon-btn");
    themeToggle.setTooltipText(isDarkTheme ? "Switch to light mode" : "Switch to dark mode");
    themeToggle.onClick(e -> toggleTheme());
    actions.add(themeToggle);

    // Logout
    IconButton logout = new IconButton(TablerIcon.create("logout"));
    logout.addClassName("toolbar-icon-btn toolbar-icon-btn--danger");
    logout.setTooltipText("Sign out");
    logout.onClick(e -> SpringSecurityFormSubmitter.logout("/logout").submit());
    actions.add(logout);

    toolbar.addToEnd(actions);
    self.addToHeader(toolbar);
  }

  private void toggleTheme() {
    isDarkTheme = !isDarkTheme;
    App.setTheme(isDarkTheme ? "dark" : "light");
    themeToggle.setName(isDarkTheme ? "moon" : "sun");
    themeToggle.setTooltipText(isDarkTheme ? "Switch to light mode" : "Switch to dark mode");
  }
}
