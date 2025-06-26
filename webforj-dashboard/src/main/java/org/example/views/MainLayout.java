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
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.toast.Toast;
import com.webforj.component.Theme;
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

    // Show demo data disclaimer toast on load
    showDemoDataToast();

    // Add demo banner to content area
    addDemoBanner();
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
    avatarImg.setSrc(
        "https://static.vecteezy.com/system/resources/previews/024/183/502/original/male-avatar-portrait-of-a-young-man-with-a-beard-illustration-of-male-character-in-modern-color-style-vector.jpg");
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
    dashboard.setPrefixComponent(FeatherIcon.GRID.create());

    AppNavItem news = new AppNavItem("News", NewsView.class);
    news.setPrefixComponent(FeatherIcon.RSS.create());

    AppNavItem analytics = new AppNavItem("Analytics & Portfolio", AnalyticsView.class);
    analytics.setPrefixComponent(FeatherIcon.PIE_CHART.create());

    AppNavItem settings = new AppNavItem("Settings", SettingsView.class);
    settings.setPrefixComponent(FeatherIcon.SETTINGS.create());

    // About as individual item
    // AppNavItem about = new AppNavItem("About", AboutView.class);
    // about.setPrefixComponent(FeatherIcon.INFO.create());

    AppNavItem about = new AppNavItem("About", "https://webforj.com/",
        FeatherIcon.INFO.create());
    about.setSuffixComponent(FeatherIcon.EXTERNAL_LINK.create());

    // Documentation - external link
    AppNavItem documentation = new AppNavItem("Documentation", "https://docs.webforj.com",
        FeatherIcon.BOOK_OPEN.create());
    documentation.setSuffixComponent(FeatherIcon.EXTERNAL_LINK.create());

    // GitHub - external link
    AppNavItem github = new AppNavItem("GitHub", "https://github.com/webforj", FeatherIcon.GITHUB.create());
    github.setSuffixComponent(FeatherIcon.EXTERNAL_LINK.create());

    // Built with webforJ - external link
    AppNavItem builtWith = new AppNavItem("Built with webforJ", "https://github.com/webforj/built-with-webforj",
        FeatherIcon.LAYERS.create());
    builtWith.setSuffixComponent(FeatherIcon.EXTERNAL_LINK.create());

    appNav.addItem(dashboard);
    appNav.addItem(news);
    appNav.addItem(analytics);
    appNav.addItem(settings);
    appNav.addItem(about);
    appNav.addItem(documentation);
    appNav.addItem(github);
    appNav.addItem(builtWith);

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
   * Finds ChartRedrawable components in the current window and redraws their
   * charts
   */
  private void findAndRedrawCharts() {
    try {
      // Find all components in the current window that implement ChartRedrawable
      self.getComponents().stream()
          .filter(ChartRedrawable.class::isInstance)
          .filter(c -> c.getClass().getSimpleName().endsWith("View")) // Only view components
          .findFirst()
          .ifPresent(view -> {
            ((ChartRedrawable) view).redrawCharts();
          });
    } catch (Exception ex) {
      App.console().log(ex.getMessage());
    }
  }

  /**
   * Shows a toast notification about demo data
   */
  private void showDemoDataToast() {
    Toast.show("📊 Demo Mode: All data displayed is for demonstration purposes only",
        Theme.GRAY);
  }

  /**
   * Adds a dismissible demo banner to the content area
   */
  private void addDemoBanner() {
    // Create banner container
    FlexLayout banner = new FlexLayout();
    banner.addClassName("demo-banner");
    banner.setAlignment(FlexAlignment.CENTER);

    // Info icon
    banner.add(FeatherIcon.INFO.create());

    // Banner text
    Div bannerText = new Div();
    bannerText.addClassName("demo-banner__text");
    bannerText.setText("Demo Mode: All cryptocurrency data shown is simulated for demonstration purposes only");
    banner.add(bannerText);

    // Close button
    IconButton closeBtn = new IconButton(FeatherIcon.X.create());
    closeBtn.addClassName("demo-banner__close");
    closeBtn.onClick(e -> banner.setVisible(false));
    banner.add(closeBtn);

    // Add banner to the content area at the top
    self.addToContent(banner);
  }
}
