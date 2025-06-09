package org.example.views;

import org.example.models.NewsArticle;
import org.example.services.NewsService;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.H4;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.component.list.ChoiceBox;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "news", outlet = MainLayout.class)
@FrameTitle("Financial News")
public class NewsView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private NewsService newsService = new NewsService();
  private List<NewsArticle> allArticles;
  private FlexLayout heroSection;
  private FlexLayout trendingSection;
  private FlexLayout mainContentArea;
  private FlexLayout sidebarArea;
  private TextField searchField;
  private ChoiceBox categoryFilter;
  private ChoiceBox timeFilter;
  private String currentCategory = "All";
  private String currentTimeFilter = "Today";
  private String searchTerm = "";
  
  // Stock images for different news categories
  private static final String[] HERO_IMAGES = {
    "https://images.unsplash.com/photo-1642790106117-e829e14a795f?w=1200&h=600&fit=crop",
    "https://images.unsplash.com/photo-1640340434855-6084b1f4901c?w=1200&h=600&fit=crop",
    "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=1200&h=600&fit=crop"
  };
  
  private static final String[] ARTICLE_IMAGES = {
    "https://images.unsplash.com/photo-1639762681485-074b7f938ba0?w=400&h=250&fit=crop",
    "https://images.unsplash.com/photo-1640826843968-011c0d0c4fbb?w=400&h=250&fit=crop",
    "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=250&fit=crop",
    "https://images.unsplash.com/photo-1642790553165-44c757d9b66d?w=400&h=250&fit=crop",
    "https://images.unsplash.com/photo-1642543348026-0c6b3e2f8b02?w=400&h=250&fit=crop"
  };

  public NewsView() {
    self.addClassName("news-view");
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("overflow-y", "auto");
    self.setStyle("padding", "0");
    self.setStyle("background", "var(--dwc-surface-1)");
    
    // Load all articles
    allArticles = newsService.getMockCryptoNews();
    
    // Create main layout structure
    createHeader();
    createHeroSection();
    createToolbar();
    createMainLayout();
    
    // Populate content
    loadContent();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.setJustifyContent(FlexJustifyContent.BETWEEN)
          .setAlignment(FlexAlignment.CENTER)
          .setStyle("background", "var(--dwc-surface-2)")
          .setStyle("padding", "var(--dwc-space-l) var(--dwc-space-xl)")
          .setStyle("border-bottom", "1px solid var(--dwc-color-gray-90)")
          .setWrap(FlexWrap.WRAP)
          .setStyle("gap", "var(--dwc-space-m)");
    
    // Brand section
    FlexLayout brand = new FlexLayout();
    brand.setAlignment(FlexAlignment.CENTER)
         .setStyle("gap", "var(--dwc-space-m)");
    
    H1 title = new H1("Market News");
    title.setStyle("margin", "0")
         .setStyle("font-size", "2rem")
         .setStyle("font-weight", "600")
         .setStyle("color", "var(--dwc-color-on-surface)");
    
    Span badge = new Span("LIVE");
    badge.setStyle("background", "var(--dwc-color-danger-40)")
         .setStyle("color", "white")
         .setStyle("padding", "2px 8px")
         .setStyle("border-radius", "4px")
         .setStyle("font-size", "12px")
         .setStyle("font-weight", "bold")
         .setStyle("animation", "pulse 2s infinite");
    
    brand.add(title, badge);
    
    // Action buttons
    FlexLayout actions = new FlexLayout();
    actions.setStyle("gap", "var(--dwc-space-s)");
    
    Button subscribeBtn = new Button("Subscribe");
    subscribeBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    subscribeBtn.setPrefixComponent(TablerIcon.create("bell"));
    
    Button refreshBtn = new Button("Refresh");
    refreshBtn.setPrefixComponent(TablerIcon.create("refresh"));
    refreshBtn.onClick(e -> refreshNews());
    
    actions.add(refreshBtn, subscribeBtn);
    
    header.add(brand, actions);
    self.add(header);
  }

  private void createHeroSection() {
    heroSection = new FlexLayout();
    heroSection.setDirection(FlexDirection.COLUMN)
               .setStyle("background", "linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)")
               .setStyle("color", "white")
               .setStyle("padding", "var(--dwc-space-xl)")
               .setStyle("position", "relative")
               .setStyle("overflow", "hidden");
    
    // Background pattern
    Div bgPattern = new Div();
    bgPattern.setStyle("position", "absolute")
             .setStyle("top", "0")
             .setStyle("left", "0")
             .setStyle("right", "0")
             .setStyle("bottom", "0")
             .setStyle("opacity", "0.1")
             .setStyle("background-image", "url('data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"20\" height=\"20\" viewBox=\"0 0 20 20\"><defs><pattern id=\"a\" x=\"0\" y=\"0\" width=\"20\" height=\"20\" patternUnits=\"userSpaceOnUse\"><circle fill=\"white\" cx=\"10\" cy=\"10\" r=\"1\"/></pattern></defs><rect width=\"100%\" height=\"100%\" fill=\"url(%23a)\"/></svg>')");
    
    heroSection.add(bgPattern);
    
    FlexLayout heroContent = new FlexLayout();
    heroContent.setJustifyContent(FlexJustifyContent.BETWEEN)
               .setAlignment(FlexAlignment.CENTER)
               .setWrap(FlexWrap.WRAP)
               .setStyle("gap", "var(--dwc-space-xl)")
               .setStyle("position", "relative")
               .setStyle("z-index", "1");
    
    // Hero text
    FlexLayout heroText = new FlexLayout();
    heroText.setDirection(FlexDirection.COLUMN)
            .setStyle("flex", "1 1 400px");
    
    H2 heroTitle = new H2("Breaking: Bitcoin Reaches New All-Time High");
    heroTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0")
             .setStyle("font-size", "2.5rem")
             .setStyle("font-weight", "700")
             .setStyle("line-height", "1.2");
    
    Paragraph heroDesc = new Paragraph("Bitcoin surged past $75,000 today as institutional adoption continues to accelerate. Market analysts predict further growth as regulatory clarity improves globally.");
    heroDesc.setStyle("margin", "0 0 var(--dwc-space-l) 0")
            .setStyle("font-size", "1.1rem")
            .setStyle("opacity", "0.9")
            .setStyle("line-height", "1.6");
    
    Anchor readMoreBtn = new Anchor("Read Full Story →");
    readMoreBtn.setHref("https://example.com/bitcoin-surge");
    readMoreBtn.setTarget("_blank");
    readMoreBtn.setStyle("color", "white");
    readMoreBtn.setStyle("border", "1px solid white");
    readMoreBtn.setStyle("border-radius", "var(--dwc-border-radius-m)");
    readMoreBtn.setStyle("padding", "var(--dwc-space-s) var(--dwc-space-m)");
    readMoreBtn.setStyle("text-decoration", "none");
    readMoreBtn.setStyle("display", "inline-block");
    readMoreBtn.setStyle("transition", "all var(--dwc-transition-fast) ease");
    
    heroText.add(heroTitle, heroDesc, readMoreBtn);
    
    // Hero image
    Div heroImageContainer = new Div();
    heroImageContainer.setStyle("flex", "1 1 300px")
                      .setStyle("height", "250px")
                      .setStyle("background-image", "url('" + HERO_IMAGES[0] + "')")
                      .setStyle("background-size", "cover")
                      .setStyle("background-position", "center")
                      .setStyle("border-radius", "var(--dwc-border-radius-l)")
                      .setStyle("box-shadow", "0 10px 30px rgba(0,0,0,0.3)");
    
    heroContent.add(heroText, heroImageContainer);
    heroSection.add(heroContent);
    self.add(heroSection);
  }

  private void createToolbar() {
    FlexLayout toolbar = new FlexLayout();
    toolbar.setJustifyContent(FlexJustifyContent.BETWEEN)
           .setAlignment(FlexAlignment.CENTER)
           .setStyle("background", "var(--dwc-surface-2)")
           .setStyle("padding", "var(--dwc-space-m) var(--dwc-space-xl)")
           .setStyle("border-bottom", "1px solid var(--dwc-color-gray-95)")
           .setWrap(FlexWrap.WRAP)
           .setStyle("gap", "var(--dwc-space-m)");
    
    // Search and filters
    FlexLayout leftSection = new FlexLayout();
    leftSection.setAlignment(FlexAlignment.CENTER)
               .setStyle("gap", "var(--dwc-space-m)")
               .setStyle("flex", "1 1 400px");
    
    searchField = new TextField();
    searchField.setPlaceholder("Search news articles...");
    searchField.setPrefixComponent(TablerIcon.create("search"));
    searchField.setStyle("flex", "0 1 200px");
    searchField.setStyle("max-width", "200px");
    searchField.onValueChange(e -> {
      searchTerm = e.getValue();
      loadContent();
    });
    
    categoryFilter = new ChoiceBox();
    categoryFilter.add("All Categories");
    categoryFilter.add("Bitcoin");
    categoryFilter.add("Ethereum");
    categoryFilter.add("DeFi");
    categoryFilter.add("Regulation");
    categoryFilter.add("Technology");
    categoryFilter.setValue("All Categories");
    categoryFilter.setStyle("width", "140px");
    categoryFilter.setStyle("min-width", "140px");
    categoryFilter.onSelect(e -> {
      currentCategory = e.getSelectedItem().getText();
      loadContent();
    });
    
    timeFilter = new ChoiceBox();
    timeFilter.add("Today");
    timeFilter.add("This Week");
    timeFilter.add("This Month");
    timeFilter.add("All Time");
    timeFilter.setValue("Today");
    timeFilter.setStyle("width", "120px");
    timeFilter.setStyle("min-width", "120px");
    timeFilter.onSelect(e -> {
      currentTimeFilter = e.getSelectedItem().getText();
      loadContent();
    });
    
    leftSection.add(searchField, categoryFilter, timeFilter);
    
    // Right section - view options
    FlexLayout rightSection = new FlexLayout();
    rightSection.setAlignment(FlexAlignment.CENTER)
                .setStyle("gap", "var(--dwc-space-s)");
    
    Span viewLabel = new Span("View:");
    viewLabel.setStyle("color", "var(--dwc-color-gray-40)")
             .setStyle("font-size", "var(--dwc-font-size-s)");
    
    Button gridViewBtn = new Button("");
    gridViewBtn.setPrefixComponent(TablerIcon.create("grid-dots"));
    gridViewBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    gridViewBtn.setStyle("min-width", "auto");
    gridViewBtn.setStyle("padding", "var(--dwc-space-xs)");
    
    Button listViewBtn = new Button("");
    listViewBtn.setPrefixComponent(TablerIcon.create("list"));
    listViewBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    listViewBtn.setStyle("min-width", "auto");
    listViewBtn.setStyle("padding", "var(--dwc-space-xs)");
    
    rightSection.add(viewLabel, gridViewBtn, listViewBtn);
    
    toolbar.add(leftSection, rightSection);
    self.add(toolbar);
  }

  private void createMainLayout() {
    FlexLayout mainContainer = new FlexLayout();
    mainContainer.setStyle("padding", "var(--dwc-space-xl)")
                 .setStyle("gap", "var(--dwc-space-xl)")
                 .setStyle("max-width", "1400px")
                 .setStyle("margin", "0 auto")
                 .setWrap(FlexWrap.WRAP);
    
    // Main content area (2/3 width)
    mainContentArea = new FlexLayout();
    mainContentArea.setDirection(FlexDirection.COLUMN)
                   .setStyle("flex", "2 1 600px")
                   .setStyle("gap", "var(--dwc-space-l)");
    
    // Sidebar area (1/3 width)
    sidebarArea = new FlexLayout();
    sidebarArea.setDirection(FlexDirection.COLUMN)
               .setStyle("flex", "1 1 300px")
               .setStyle("gap", "var(--dwc-space-l)");
    
    mainContainer.add(mainContentArea, sidebarArea);
    self.add(mainContainer);
  }

  private void loadContent() {
    mainContentArea.removeAll();
    sidebarArea.removeAll();
    
    // Create trending section
    createTrendingSection();
    
    // Create main articles
    createMainArticles();
    
    // Create sidebar content
    createSidebar();
  }
  
  private void createTrendingSection() {
    Div trendingContainer = new Div();
    trendingContainer.setStyle("background", "var(--dwc-surface-2)")
                     .setStyle("border-radius", "var(--dwc-border-radius-l)")
                     .setStyle("padding", "var(--dwc-space-l)")
                     .setStyle("border", "1px solid var(--dwc-color-gray-90)");
    
    FlexLayout trendingHeader = new FlexLayout();
    trendingHeader.setJustifyContent(FlexJustifyContent.BETWEEN)
                  .setAlignment(FlexAlignment.CENTER)
                  .setStyle("margin-bottom", "var(--dwc-space-m)");
    
    FlexLayout titleWithIcon = new FlexLayout();
    titleWithIcon.setAlignment(FlexAlignment.CENTER)
                 .setStyle("gap", "var(--dwc-space-s)");
    
    IconButton trendingIcon = new IconButton(TablerIcon.create("trending-up"));
    trendingIcon.setStyle("color", "var(--dwc-color-danger-40)");
    
    H3 trendingTitle = new H3("Trending Now");
    trendingTitle.setStyle("margin", "0")
                 .setStyle("color", "var(--dwc-color-on-surface)");
    
    titleWithIcon.add(trendingIcon, trendingTitle);
    
    Anchor viewAllLink = new Anchor("View All");
    viewAllLink.setStyle("color", "var(--dwc-color-primary-40)")
               .setStyle("text-decoration", "none")
               .setStyle("font-size", "var(--dwc-font-size-s)");
    
    trendingHeader.add(titleWithIcon, viewAllLink);
    
    FlexLayout trendingGrid = new FlexLayout();
    trendingGrid.setStyle("gap", "var(--dwc-space-m)")
                .setWrap(FlexWrap.WRAP);
    
    // Add trending articles (smaller cards)
    for (int i = 0; i < Math.min(3, allArticles.size()); i++) {
      NewsArticle article = allArticles.get(i);
      Div trendingCard = createTrendingCard(article, i + 1);
      trendingGrid.add(trendingCard);
    }
    
    trendingContainer.add(trendingHeader, trendingGrid);
    mainContentArea.add(trendingContainer);
  }
  
  private Div createTrendingCard(NewsArticle article, int rank) {
    Div card = new Div();
    card.setStyle("flex", "1 1 200px")
        .setStyle("background", "var(--dwc-surface-1)")
        .setStyle("border-radius", "var(--dwc-border-radius-m)")
        .setStyle("padding", "var(--dwc-space-m)")
        .setStyle("border", "1px solid var(--dwc-color-gray-95)")
        .setStyle("cursor", "pointer")
        .setStyle("transition", "all var(--dwc-transition-medium) ease");
    
    FlexLayout cardHeader = new FlexLayout();
    cardHeader.setJustifyContent(FlexJustifyContent.BETWEEN)
              .setAlignment(FlexAlignment.START)
              .setStyle("margin-bottom", "var(--dwc-space-s)");
    
    Span rankBadge = new Span(String.valueOf(rank));
    rankBadge.setStyle("background", "var(--dwc-color-primary-40)")
             .setStyle("color", "white")
             .setStyle("width", "24px")
             .setStyle("height", "24px")
             .setStyle("border-radius", "50%")
             .setStyle("display", "flex")
             .setStyle("align-items", "center")
             .setStyle("justify-content", "center")
             .setStyle("font-size", "12px")
             .setStyle("font-weight", "bold");
    
    Span timeAgo = new Span(article.getTimeAgo());
    timeAgo.setStyle("font-size", "var(--dwc-font-size-xs)")
           .setStyle("color", "var(--dwc-color-gray-40)");
    
    cardHeader.add(rankBadge, timeAgo);
    
    H4 title = new H4(article.getTitle());
    title.setStyle("margin", "0 0 var(--dwc-space-xs) 0")
         .setStyle("font-size", "var(--dwc-font-size-m)")
         .setStyle("line-height", "1.3")
         .setStyle("display", "-webkit-box")
         .setStyle("-webkit-line-clamp", "2")
         .setStyle("-webkit-box-orient", "vertical")
         .setStyle("overflow", "hidden");
    
    Span source = new Span(article.getSource());
    source.setStyle("font-size", "var(--dwc-font-size-xs)")
          .setStyle("color", "var(--dwc-color-primary-40)")
          .setStyle("font-weight", "500");
    
    card.add(cardHeader, title, source);
    return card;
  }
  
  private void createMainArticles() {
    Div articlesContainer = new Div();
    articlesContainer.setStyle("background", "var(--dwc-surface-2)")
                     .setStyle("border-radius", "var(--dwc-border-radius-l)")
                     .setStyle("padding", "var(--dwc-space-l)")
                     .setStyle("border", "1px solid var(--dwc-color-gray-90)");
    
    H3 articlesTitle = new H3("Latest Articles");
    articlesTitle.setStyle("margin", "0 0 var(--dwc-space-l) 0")
                 .setStyle("color", "var(--dwc-color-on-surface)");
    
    FlexLayout articlesGrid = new FlexLayout();
    articlesGrid.setDirection(FlexDirection.COLUMN)
                .setStyle("gap", "var(--dwc-space-l)");
    
    // Filter articles
    List<NewsArticle> filteredArticles = allArticles.stream()
        .filter(article -> matchesCategory(article, currentCategory))
        .filter(article -> matchesSearch(article, searchTerm))
        .limit(6)
        .collect(Collectors.toList());
    
    if (filteredArticles.isEmpty()) {
      Div noResults = new Div();
      noResults.setStyle("text-align", "center")
               .setStyle("padding", "var(--dwc-space-xl)")
               .setStyle("color", "var(--dwc-color-gray-40)");
      
      IconButton searchIcon = new IconButton(TablerIcon.create("search-off"));
      searchIcon.setStyle("font-size", "3rem")
                .setStyle("margin-bottom", "var(--dwc-space-m)")
                .setStyle("opacity", "0.5");
      
      H4 noResultsTitle = new H4("No articles found");
      noResultsTitle.setStyle("margin", "0 0 var(--dwc-space-s) 0");
      
      Paragraph noResultsText = new Paragraph("Try adjusting your search criteria or filters");
      noResultsText.setStyle("margin", "0");
      
      noResults.add(searchIcon, noResultsTitle, noResultsText);
      articlesGrid.add(noResults);
    } else {
      for (int i = 0; i < filteredArticles.size(); i++) {
        NewsArticle article = filteredArticles.get(i);
        Div articleCard = createMainArticleCard(article, i);
        articlesGrid.add(articleCard);
      }
    }
    
    articlesContainer.add(articlesTitle, articlesGrid);
    mainContentArea.add(articlesContainer);
  }
  
  private Div createMainArticleCard(NewsArticle article, int index) {
    Div card = new Div();
    card.setStyle("background", "var(--dwc-surface-1)")
        .setStyle("border-radius", "var(--dwc-border-radius-m)")
        .setStyle("border", "1px solid var(--dwc-color-gray-95)")
        .setStyle("overflow", "hidden")
        .setStyle("cursor", "pointer")
        .setStyle("transition", "all var(--dwc-transition-medium) ease");
    
    FlexLayout cardContent = new FlexLayout();
    cardContent.setStyle("gap", "var(--dwc-space-m)")
               .setWrap(FlexWrap.WRAP);
    
    // Article image
    Div imageContainer = new Div();
    imageContainer.setStyle("flex", "0 0 200px")
                  .setStyle("height", "120px")
                  .setStyle("background-image", "url('" + (article.getImageUrl() != null ? article.getImageUrl() : "") + "')")
                  .setStyle("background-size", "cover")
                  .setStyle("background-position", "center")
                  .setStyle("border-radius", "var(--dwc-border-radius-s)");
    
    // Article content
    FlexLayout textContent = new FlexLayout();
    textContent.setDirection(FlexDirection.COLUMN)
               .setStyle("flex", "1 1 300px")
               .setStyle("padding", "var(--dwc-space-m)");
    
    // Meta info
    FlexLayout metaInfo = new FlexLayout();
    metaInfo.setJustifyContent(FlexJustifyContent.BETWEEN)
            .setAlignment(FlexAlignment.CENTER)
            .setStyle("margin-bottom", "var(--dwc-space-s)");
    
    Span source = new Span(article.getSource());
    source.setStyle("color", "var(--dwc-color-primary-40)")
          .setStyle("font-weight", "600")
          .setStyle("font-size", "var(--dwc-font-size-s)");
    
    Span timeAgo = new Span(article.getTimeAgo());
    timeAgo.setStyle("color", "var(--dwc-color-gray-40)")
           .setStyle("font-size", "var(--dwc-font-size-xs)");
    
    metaInfo.add(source, timeAgo);
    
    // Title
    H4 title = new H4(article.getTitle());
    title.setStyle("margin", "0 0 var(--dwc-space-s) 0")
         .setStyle("font-size", "var(--dwc-font-size-l)")
         .setStyle("font-weight", "600")
         .setStyle("line-height", "1.3")
         .setStyle("color", "var(--dwc-color-on-surface)");
    
    // Description
    Paragraph description = new Paragraph(article.getDescription());
    description.setStyle("margin", "0 0 var(--dwc-space-m) 0")
               .setStyle("color", "var(--dwc-color-gray-25)")
               .setStyle("line-height", "1.5")
               .setStyle("display", "-webkit-box")
               .setStyle("-webkit-line-clamp", "2")
               .setStyle("-webkit-box-orient", "vertical")
               .setStyle("overflow", "hidden");
    
    // Read more link
    Anchor readMore = new Anchor("Read more");
    readMore.setHref(article.getUrl())
            .setTarget("_blank")
            .setStyle("color", "var(--dwc-color-primary-40)")
            .setStyle("text-decoration", "none")
            .setStyle("font-weight", "500")
            .setStyle("font-size", "var(--dwc-font-size-s)");
    
    textContent.add(metaInfo, title, description, readMore);
    cardContent.add(imageContainer, textContent);
    card.add(cardContent);
    
    return card;
  }
  
  private void createSidebar() {
    // Market highlights
    createMarketHighlights();
    
    // Popular articles
    createPopularArticles();
    
    // Newsletter signup
    createNewsletterSignup();
  }
  
  private void createMarketHighlights() {
    Div marketContainer = new Div();
    marketContainer.setStyle("background", "var(--dwc-surface-2)")
                   .setStyle("border-radius", "var(--dwc-border-radius-l)")
                   .setStyle("padding", "var(--dwc-space-l)")
                   .setStyle("border", "1px solid var(--dwc-color-gray-90)");
    
    H3 marketTitle = new H3("Market Highlights");
    marketTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0")
               .setStyle("color", "var(--dwc-color-on-surface)");
    
    FlexLayout highlights = new FlexLayout();
    highlights.setDirection(FlexDirection.COLUMN)
              .setStyle("gap", "var(--dwc-space-m)");
    
    String[][] marketData = {
      {"Bitcoin", "$67,234", "+2.4%", "success"},
      {"Ethereum", "$2,891", "-0.8%", "danger"},
      {"Market Cap", "$2.1T", "+1.2%", "success"}
    };
    
    for (String[] data : marketData) {
      FlexLayout item = new FlexLayout();
      item.setJustifyContent(FlexJustifyContent.BETWEEN)
          .setAlignment(FlexAlignment.CENTER)
          .setStyle("padding", "var(--dwc-space-s)")
          .setStyle("background", "var(--dwc-surface-1)")
          .setStyle("border-radius", "var(--dwc-border-radius-s)")
          .setStyle("border", "1px solid var(--dwc-color-gray-95)");
      
      FlexLayout leftSide = new FlexLayout();
      leftSide.setDirection(FlexDirection.COLUMN);
      
      Span name = new Span(data[0]);
      name.setStyle("font-size", "var(--dwc-font-size-s)")
          .setStyle("color", "var(--dwc-color-gray-40)");
      
      Span price = new Span(data[1]);
      price.setStyle("font-weight", "600")
           .setStyle("color", "var(--dwc-color-on-surface)");
      
      leftSide.add(name, price);
      
      Span change = new Span(data[2]);
      change.setStyle("font-size", "var(--dwc-font-size-s)")
            .setStyle("font-weight", "600")
            .setStyle("color", data[3].equals("success") ? "var(--dwc-color-success-25)" : "var(--dwc-color-danger-40)");
      
      item.add(leftSide, change);
      highlights.add(item);
    }
    
    marketContainer.add(marketTitle, highlights);
    sidebarArea.add(marketContainer);
  }
  
  private void createPopularArticles() {
    Div popularContainer = new Div();
    popularContainer.setStyle("background", "var(--dwc-surface-2)")
                    .setStyle("border-radius", "var(--dwc-border-radius-l)")
                    .setStyle("padding", "var(--dwc-space-l)")
                    .setStyle("border", "1px solid var(--dwc-color-gray-90)");
    
    H3 popularTitle = new H3("Most Popular");
    popularTitle.setStyle("margin", "0 0 var(--dwc-space-m) 0")
                .setStyle("color", "var(--dwc-color-on-surface)");
    
    FlexLayout popularList = new FlexLayout();
    popularList.setDirection(FlexDirection.COLUMN)
               .setStyle("gap", "var(--dwc-space-m)");
    
    for (int i = 0; i < Math.min(4, allArticles.size()); i++) {
      NewsArticle article = allArticles.get(i);
      
      FlexLayout item = new FlexLayout();
      item.setStyle("gap", "var(--dwc-space-s)")
          .setStyle("cursor", "pointer")
          .setStyle("padding", "var(--dwc-space-s)")
          .setStyle("border-radius", "var(--dwc-border-radius-s)")
          .setStyle("transition", "background var(--dwc-transition-fast) ease");
      
      Span rank = new Span(String.valueOf(i + 1));
      rank.setStyle("color", "var(--dwc-color-primary-40)")
          .setStyle("font-weight", "bold")
          .setStyle("font-size", "var(--dwc-font-size-s)");
      
      FlexLayout content = new FlexLayout();
      content.setDirection(FlexDirection.COLUMN)
             .setStyle("flex", "1");
      
      H4 title = new H4(article.getTitle());
      title.setStyle("margin", "0 0 var(--dwc-space-2xs) 0")
           .setStyle("font-size", "var(--dwc-font-size-s)")
           .setStyle("line-height", "1.3")
           .setStyle("display", "-webkit-box")
           .setStyle("-webkit-line-clamp", "2")
           .setStyle("-webkit-box-orient", "vertical")
           .setStyle("overflow", "hidden");
      
      Span timeAgo = new Span(article.getTimeAgo());
      timeAgo.setStyle("font-size", "var(--dwc-font-size-xs)")
             .setStyle("color", "var(--dwc-color-gray-40)");
      
      content.add(title, timeAgo);
      item.add(rank, content);
      
      popularList.add(item);
    }
    
    popularContainer.add(popularTitle, popularList);
    sidebarArea.add(popularContainer);
  }
  
  private void createNewsletterSignup() {
    Div newsletterContainer = new Div();
    newsletterContainer.setStyle("background", "linear-gradient(135deg, #3b82f6, #2563eb)")
                       .setStyle("border-radius", "var(--dwc-border-radius-l)")
                       .setStyle("padding", "var(--dwc-space-l)")
                       .setStyle("color", "white")
                       .setStyle("text-align", "center");
    
    IconButton emailIcon = new IconButton(TablerIcon.create("mail"));
    emailIcon.setStyle("font-size", "2rem")
             .setStyle("margin-bottom", "var(--dwc-space-m)");
    
    H3 newsletterTitle = new H3("Stay Informed");
    newsletterTitle.setStyle("margin", "0 0 var(--dwc-space-s) 0")
                   .setStyle("color", "white");
    
    Paragraph newsletterText = new Paragraph("Get the latest crypto news delivered to your inbox daily.");
    newsletterText.setStyle("margin", "0 0 var(--dwc-space-m) 0")
                  .setStyle("opacity", "0.9")
                  .setStyle("font-size", "var(--dwc-font-size-s)");
    
    TextField emailField = new TextField();
    emailField.setPlaceholder("Enter your email");
    emailField.setStyle("margin-bottom", "var(--dwc-space-m)");
    
    Button subscribeBtn = new Button("Subscribe");
    subscribeBtn.setTheme(ButtonTheme.DEFAULT);
    subscribeBtn.setStyle("border-color", "white");
    subscribeBtn.setStyle("width", "100%");
    
    newsletterContainer.add(emailIcon, newsletterTitle, newsletterText, emailField, subscribeBtn);
    sidebarArea.add(newsletterContainer);
  }
  
  private boolean matchesCategory(NewsArticle article, String category) {
    if ("All Categories".equals(category)) {
      return true;
    }
    
    String title = article.getTitle().toLowerCase();
    String description = article.getDescription().toLowerCase();
    String content = title + " " + description;
    
    switch (category) {
      case "Bitcoin":
        return content.contains("bitcoin") || content.contains("btc");
      case "Ethereum":
        return content.contains("ethereum") || content.contains("eth");
      case "DeFi":
        return content.contains("defi") || content.contains("decentralized finance") || 
               content.contains("yield") || content.contains("liquidity");
      case "Regulation":
        return content.contains("regulation") || content.contains("sec") || 
               content.contains("government") || content.contains("compliance");
      case "Technology":
        return content.contains("blockchain") || content.contains("technology") || 
               content.contains("protocol") || content.contains("upgrade");
      default:
        return true;
    }
  }

  private boolean matchesSearch(NewsArticle article, String search) {
    if (search == null || search.trim().isEmpty()) {
      return true;
    }
    
    String searchLower = search.toLowerCase();
    return article.getTitle().toLowerCase().contains(searchLower) ||
           article.getDescription().toLowerCase().contains(searchLower) ||
           article.getSource().toLowerCase().contains(searchLower);
  }

  private void refreshNews() {
    // Simulate refreshing news
    allArticles = newsService.getMockCryptoNews();
    loadContent();
  }
}