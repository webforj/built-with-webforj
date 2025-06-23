package org.example.views;

import java.util.Set;

import com.webforj.App;
import com.webforj.Interval;
import com.webforj.annotation.StyleSheet;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Img;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.applayout.AppLayout.DrawerPlacement;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.layout.appnav.AppNav;
import com.webforj.component.layout.appnav.AppNavItem;
import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import org.example.components.DrawerLogo;
import org.example.utils.charts.ChartRedrawable;
import org.example.components.DrawerFooter;

import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;

@Route
@StyleSheet("ws://main-layout.css")
public class MainLayout extends Composite<AppLayout> {
  private final AppLayout self = getBoundComponent();
  private final H1 title = new H1();
  private boolean isDarkTheme = false;
  private IconButton themeToggle;

  public MainLayout() {
    isDarkTheme = "dark".equals(App.getTheme());
    setHeader();
    setNavDrawer();
    self.setDrawerOpened(true);
    self.addClassName("main-layout");
    Router.getCurrent().onNavigate(this::onNavigate);
    
    // Add drawer event listeners for chart redrawing
    self.onDrawerOpen(e -> redrawChartsInCurrentView());
    self.onDrawerClose(e -> redrawChartsInCurrentView());
  }

  private void setHeader() {
    Toolbar toolbar = new Toolbar();
    
    // Add proper drawer toggle
    toolbar.addToStart(new AppDrawerToggle());
    toolbar.addToTitle(title);
    getBoundComponent().setHeaderOffscreen(false);
    
    // Create main toolbar actions (left group)
    FlexLayout mainActions = new FlexLayout();
    mainActions.addClassName("main-layout__toolbar-actions");
    mainActions.setAlignment(FlexAlignment.CENTER);
    
    // Notifications
    IconButton notificationsBtn = new IconButton(TablerIcon.create("bell"));
    notificationsBtn.addClassName("main-layout__toolbar-btn");
    
    // Help/Support
    IconButton helpBtn = new IconButton(TablerIcon.create("help-circle"));
    helpBtn.addClassName("main-layout__toolbar-btn");
    
    // Profile menu (avatar)
    Div profileBtn = new Div();
    profileBtn.addClassName("main-layout__toolbar-btn main-layout__toolbar-btn--clickable");
    Img avatarImg = new Img();
    avatarImg.setSrc("https://static.vecteezy.com/system/resources/previews/024/183/502/original/male-avatar-portrait-of-a-young-man-with-a-beard-illustration-of-male-character-in-modern-color-style-vector.jpg");
    avatarImg.setAlt("Profile Avatar");
    avatarImg.setStyle("width", "24px");
    avatarImg.setStyle("height", "24px");
    avatarImg.setStyle("border-radius", "50%");
    avatarImg.setStyle("object-fit", "cover");
    profileBtn.add(avatarImg);
    
    mainActions.add(notificationsBtn, helpBtn, profileBtn);
    
    // Theme toggle (separated on the right)
    themeToggle = new IconButton(TablerIcon.create(isDarkTheme ? "moon" : "sun"));
    themeToggle.addClassName("main-layout__toolbar-btn main-layout__toolbar-btn--theme");
    themeToggle.onClick(e -> toggleTheme());
    
    // Create container for all toolbar items
    FlexLayout toolbarContainer = new FlexLayout();
    toolbarContainer.addClassName("main-layout__toolbar-container");
    toolbarContainer.setAlignment(FlexAlignment.CENTER);
    toolbarContainer.add(mainActions, themeToggle);
    
    toolbar.addToEnd(toolbarContainer);

    self.addToHeader(toolbar);
  }

  private void setNavDrawer() {
    // Create drawer container with FlexLayout
    FlexLayout drawerLayout = new FlexLayout();
    drawerLayout.addClassName("main-layout__drawer");
    drawerLayout.setDirection(FlexDirection.COLUMN);
    
    // Add logo
    drawerLayout.add(new DrawerLogo());
    
    AppNav appNav = new AppNav();
    appNav.addClassName("main-layout__nav");
    appNav.setAutoOpen(true);
    
    AppNavItem dashboard = new AppNavItem("Dashboard", DashboardView.class);
    dashboard.setPrefixComponent(TablerIcon.create("dashboard"));
    
    AppNavItem news = new AppNavItem("News", NewsView.class);
    news.setPrefixComponent(TablerIcon.create("news"));
    
    AppNavItem analytics = new AppNavItem("Analytics & Portfolio", AnalyticsView.class);
    analytics.setPrefixComponent(TablerIcon.create("chart-pie"));
    
    AppNavItem settings = new AppNavItem("Settings", SettingsView.class);
    settings.setPrefixComponent(TablerIcon.create("settings"));
    
    // About section with expandable children
    AppNavItem about = new AppNavItem("About");
    about.setPrefixComponent(TablerIcon.create("info-circle"));
    
    // Overview item links to the About view
    AppNavItem overview = new AppNavItem("Overview", AboutView.class);
    overview.setPrefixComponent(TablerIcon.create("home"));
    about.addItem(overview);
    
    // Documentation - external link
    about.addItem(new AppNavItem("Documentation", "https://docs.webforj.com", TablerIcon.create("book"))
        .setSuffixComponent(TablerIcon.create("external-link")));
    
    // GitHub - external link
    about.addItem(new AppNavItem("GitHub", "https://github.com/webforj", TablerIcon.create("brand-github"))
        .setSuffixComponent(TablerIcon.create("external-link")));
    
    // Built with webforJ - external link
    about.addItem(new AppNavItem("Built with webforJ", "https://github.com/webforj/built-with-webforj", TablerIcon.create("apps"))
        .setSuffixComponent(TablerIcon.create("external-link")));
    
    appNav.addItem(dashboard);
    appNav.addItem(news);
    appNav.addItem(analytics);
    appNav.addItem(settings);
    appNav.addItem(about);
    
    drawerLayout.add(appNav);
    drawerLayout.add(new DrawerFooter());
    
    self.setDrawerHeaderVisible(false);
    self.addToDrawer(drawerLayout);
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
  
  /**
   * Triggers chart redraw in the current view if it implements ChartRedrawable
   */
  private void redrawChartsInCurrentView() {
    // Small delay to allow drawer animation to complete    
    // Use an interval that fires once after a delay
    Interval interval = new Interval(0.2f, e -> {
      // Stop the interval immediately since we only want it to fire once
      e.getInterval().stop();
      
      // Find and redraw charts in the current view
      findAndRedrawCharts();
    });
    interval.start();
  }
  
  /**
   * Finds ChartRedrawable components in the current window and redraws their charts
   */
  private void findAndRedrawCharts() {
    try {
      // Find all components in the current window that implement ChartRedrawable
      self.getComponents().stream()
          .filter(c -> c instanceof ChartRedrawable)
          .filter(c -> c.getClass().getSimpleName().endsWith("View")) // Only view components
          .findFirst()
          .ifPresent(view -> {
            ((ChartRedrawable) view).redrawCharts();
          });
    } catch (Exception ex) {
    }
  }
}