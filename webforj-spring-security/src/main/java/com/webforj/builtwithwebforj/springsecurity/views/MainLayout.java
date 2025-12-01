package com.webforj.builtwithwebforj.springsecurity.views;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.components.UserInfo;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.spring.security.SpringSecurityFormSubmitter;

import jakarta.annotation.security.PermitAll;

@Route
@PermitAll
@StyleSheet("ws://main-layout.css")
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();

  public MainLayout() {
    self.setDrawerPlacement(AppLayout.DrawerPlacement.HIDDEN);
    self.setHeaderOffscreen(false);
    setupHeader();
  }

  private void setupHeader() {
    Toolbar toolbar = new Toolbar();
    toolbar.addClassName("toolbar");

    // Logo/brand
    FlexLayout brand = FlexLayout.create(
        TablerIcon.create("ticket"),
        new Span("Support Desk")
    ).horizontal().align().center().build();
    brand.addClassName("brand");
    brand.setSpacing("var(--dwc-space-s)");
    brand.onClick(e -> Router.getCurrent().navigate(DashboardView.class));
    toolbar.addToStart(brand);

    // Right side controls
    FlexLayout controls = FlexLayout.create().horizontal().align().center().build();
    controls.setSpacing("var(--dwc-space-m)");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = auth != null && auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

    if (isAdmin) {
      Button adminButton = new Button("Users");
      adminButton.setPrefixComponent(TablerIcon.create("users"));
      adminButton.setTheme(ButtonTheme.DEFAULT);
      adminButton.addClassName("header-button");
      adminButton.onClick(e -> Router.getCurrent().navigate(AdminUsersView.class));
      controls.add(adminButton);
    }

    UserInfo userInfo = new UserInfo();
    controls.add(userInfo);

    Button logout = new Button("Logout");
    logout.setPrefixComponent(TablerIcon.create("logout"));
    logout.setTheme(ButtonTheme.DEFAULT);
    logout.addClassName("header-button");
    logout.onClick(e -> SpringSecurityFormSubmitter.logout("/logout").submit());
    controls.add(logout);

    toolbar.addToEnd(controls);
    self.addToHeader(toolbar);
  }
}
