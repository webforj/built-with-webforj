package org.example.views;

import java.util.Set;

import com.webforj.App;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.applayout.AppLayout.DrawerPlacement;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.layout.appnav.AppNav;
import com.webforj.component.layout.appnav.AppNavItem;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import org.example.components.DrawerLogo;
import org.example.components.DrawerFooter;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;

@Route
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private H1 title = new H1();
  private boolean isDarkTheme = false;
  private IconButton themeToggle;
  private IconButton drawerToggle;
  private AppNav appNav;

  public MainLayout() {
    String currentTheme = App.getTheme();
    isDarkTheme = "dark".equals(currentTheme);
    
    setHeader();
    setNavDrawer();
    
    // Start with drawer open on desktop
    self.setDrawerOpened(true);
    
    // Enable scrolling for the content area
    self.setStyle("height", "100vh");
    self.setStyle("overflow", "hidden");
    
    Router.getCurrent().onNavigate(this::onNavigate);
  }

  private void setHeader() {
    Toolbar toolbar = new Toolbar();
    
    // Add proper drawer toggle
    toolbar.addToStart(new AppDrawerToggle());
    toolbar.addToTitle(title);
    
    themeToggle = new IconButton(TablerIcon.create(isDarkTheme ? "moon" : "sun"));
    themeToggle.onClick(e -> toggleTheme());
    
    toolbar.addToEnd(themeToggle);

    self.addToHeader(toolbar);
  }

  private void setNavDrawer() {
    // Create drawer container with FlexLayout
    FlexLayout drawerLayout = new FlexLayout();
    drawerLayout.setDirection(FlexDirection.COLUMN);
    drawerLayout.setStyle("height", "100%");
    drawerLayout.setStyle("display", "flex");
    
    // Add logo
    drawerLayout.add(new DrawerLogo());
    
    // Create navigation
    appNav = new AppNav();
    appNav.setStyle("flex", "1");
    
    // Dashboard
    AppNavItem dashboard = new AppNavItem("Dashboard", DashboardView.class);
    dashboard.setPrefixComponent(TablerIcon.create("dashboard"));
    
    // News
    AppNavItem news = new AppNavItem("News", NewsView.class);
    news.setPrefixComponent(TablerIcon.create("news"));
    
    // Analytics
    AppNavItem analytics = new AppNavItem("Analytics", AnalyticsView.class);
    analytics.setPrefixComponent(TablerIcon.create("chart-line"));
    
    // Portfolio
    AppNavItem portfolio = new AppNavItem("Portfolio", PortfolioView.class);
    portfolio.setPrefixComponent(TablerIcon.create("wallet"));
    
    // Settings
    AppNavItem settings = new AppNavItem("Settings", SettingsView.class);
    settings.setPrefixComponent(TablerIcon.create("settings"));
    
    // Add all items to navigation
    appNav.addItem(dashboard);
    appNav.addItem(news);
    appNav.addItem(analytics);
    appNav.addItem(portfolio);
    appNav.addItem(settings);
    
    // Add navigation to drawer
    drawerLayout.add(appNav);
    
    // Add footer component
    drawerLayout.add(new DrawerFooter());
    
    // Configure drawer
    self.setDrawerHeaderVisible(false);
    self.addToDrawer(drawerLayout);
    
    // Set drawer to left side (default)
    self.setDrawerPlacement(DrawerPlacement.LEFT);
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
  
  private void toggleTheme() {
    isDarkTheme = !isDarkTheme;
    App.setTheme(isDarkTheme ? "dark" : "light");
    themeToggle.setName(isDarkTheme ? "moon" : "sun");
  }
}