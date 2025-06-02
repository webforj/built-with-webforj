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
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Div;
import org.example.components.NewsCard;
import org.example.services.NewsService;
import org.example.models.NewsArticle;
import java.util.List;

@Route
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private H1 title = new H1();
  private boolean isDarkTheme = false;
  private IconButton themeToggle;
  private IconButton newsToggle;
  private NewsService newsService = new NewsService();

  public MainLayout() {
    String currentTheme = App.getTheme();
    isDarkTheme = "dark".equals(currentTheme);
    
    setHeader();
    setNewsDrawer();
    // Start with drawer closed
    self.setDrawerOpened(false);
    
    // Enable scrolling for the content area
    self.setStyle("height", "100vh");
    self.setStyle("overflow", "hidden");
    
    Router.getCurrent().onNavigate(this::onNavigate);
  }

  private void setHeader() {
    Toolbar toolbar = new Toolbar();
    // Remove drawer toggle since drawer is hidden
    toolbar.addToTitle(title);
    
    // News toggle button for drawer
    newsToggle = new IconButton(TablerIcon.create("news"));
    newsToggle.setAttribute("data-drawer-toggle", "true");
    newsToggle.setTooltipText("Toggle News");
    
    themeToggle = new IconButton(TablerIcon.create(isDarkTheme ? "moon" : "sun"));
    themeToggle.onClick(e -> toggleTheme());
    
    toolbar.addToEnd(newsToggle, themeToggle);

    self.addToHeader(toolbar);
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
    themeToggle.setName((isDarkTheme ? "moon" : "sun"));
  }
  
  private void setNewsDrawer() {
    // Create drawer header
    H2 drawerTitle = new H2("Crypto News");
    drawerTitle.addClassName("news-drawer-title");
    
    // Create scrollable container for news
    FlexLayout newsContainer = new FlexLayout();
    newsContainer.setDirection(FlexDirection.COLUMN)
                 .setStyle("overflow-y", "auto")
                 .setStyle("height", "calc(100vh - 100px)");
    
    // Add news articles
    List<NewsArticle> articles = newsService.getMockCryptoNews();
    for (NewsArticle article : articles) {
      NewsCard card = new NewsCard(
        article.getTitle(),
        article.getDescription(),
        article.getSource(),
        article.getTimeAgo(),
        article.getUrl()
      );
      newsContainer.add(card);
    }
    
    // Create drawer content wrapper
    FlexLayout drawerContent = new FlexLayout();
    drawerContent.setDirection(FlexDirection.COLUMN)
                 .setStyle("height", "100%")
                 .setStyle("background-color", "var(--dwc-surface-1)")
                 .add(drawerTitle, newsContainer);
    
    // Configure drawer
    self.setDrawerHeaderVisible(false);
    self.addToDrawer(drawerContent);
    
    // Set drawer to right side
    self.setDrawerPlacement(DrawerPlacement.RIGHT);
    
    // Style the drawer
    self.setStyle("--dwc-app-layout-drawer-width", "400px");
  }
}
