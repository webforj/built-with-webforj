package org.example.views;

import org.example.models.NewsArticle;
import org.example.services.NewsService;

import com.webforj.annotation.StyleSheet;
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
@StyleSheet("ws://news-view.css")
@FrameTitle("Financial News")
public class NewsView extends Composite<FlexLayout> {

  private static final String LINK_TARGET_BLANK = "_blank";

  // Category constants
  private static final String CATEGORY_ALL = "All Categories";
  private static final String CATEGORY_BITCOIN = "Bitcoin";
  private static final String CATEGORY_ETHEREUM = "Ethereum";
  private static final String CATEGORY_DEFI = "DeFi";
  private static final String CATEGORY_REGULATION = "Regulation";
  private static final String CATEGORY_TECHNOLOGY = "Technology";

  // Time filter constants
  private static final String TIME_TODAY = "Today";
  private static final String TIME_WEEK = "This Week";
  private static final String TIME_MONTH = "This Month";
  private static final String TIME_ALL = "All Time";

  // Limits
  private static final int TRENDING_ARTICLES_LIMIT = 3;
  private static final int POPULAR_ARTICLES_LIMIT = 4;
  private static final int MAIN_ARTICLES_LIMIT = 6;
  private FlexLayout self = getBoundComponent();
  private NewsService newsService = new NewsService();
  private List<NewsArticle> allArticles;
  private FlexLayout heroSection;
  private FlexLayout mainContentArea;
  private FlexLayout sidebarArea;
  private TextField searchField;
  private ChoiceBox categoryFilter;
  private ChoiceBox timeFilter;
  private String currentCategory = CATEGORY_ALL;
  private String currentTimeFilter = TIME_TODAY;
  private String searchTerm = "";

  // Stock images for different news categories
  private static final String[] HERO_IMAGES = {
      "https://images.unsplash.com/photo-1642790106117-e829e14a795f?w=1200&h=600&fit=crop",
      "https://images.unsplash.com/photo-1640340434855-6084b1f4901c?w=1200&h=600&fit=crop",
      "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=1200&h=600&fit=crop"
  };

  public NewsView() {
    self.addClassName("news-view");
    self.setDirection(FlexDirection.COLUMN);

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
    header.addClassName("news-view__header");
    header.setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER)
        .setWrap(FlexWrap.WRAP);

    // Brand section
    FlexLayout brand = new FlexLayout();
    brand.addClassName("news-view__brand");
    brand.setAlignment(FlexAlignment.CENTER);

    H1 title = new H1("Market News");
    title.addClassName("news-view__title");

    Span badge = new Span("LIVE");
    badge.addClassName("news-view__badge");

    brand.add(title, badge);

    // Action buttons
    FlexLayout actions = new FlexLayout();
    actions.addClassName("news-view__actions");

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
    heroSection.addClassName("news-view__hero");
    heroSection.setDirection(FlexDirection.COLUMN);

    // Background pattern
    Div bgPattern = new Div();
    bgPattern.addClassName("news-view__hero-pattern");

    heroSection.add(bgPattern);

    FlexLayout heroContent = new FlexLayout();
    heroContent.addClassName("news-view__hero-content");
    heroContent.setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER)
        .setWrap(FlexWrap.WRAP);

    // Hero text
    FlexLayout heroText = new FlexLayout();
    heroText.addClassName("news-view__hero-text");
    heroText.setDirection(FlexDirection.COLUMN);

    H2 heroTitle = new H2("Breaking: Bitcoin Reaches New All-Time High");
    heroTitle.addClassName("news-view__hero-title");

    Paragraph heroDesc = new Paragraph(
        "Bitcoin surged past $75,000 today as institutional adoption continues to accelerate. Market analysts predict further growth as regulatory clarity improves globally.");
    heroDesc.addClassName("news-view__hero-desc");

    Anchor readMoreBtn = new Anchor("Read Full Story →");
    readMoreBtn.addClassName("news-view__hero-button");
    readMoreBtn.setHref("https://example.com/bitcoin-surge");
    readMoreBtn.setTarget(LINK_TARGET_BLANK);

    heroText.add(heroTitle, heroDesc, readMoreBtn);

    // Hero image
    Div heroImageContainer = new Div();
    heroImageContainer.addClassName("news-view__hero-image");
    heroImageContainer.setStyle("background-image", "url('" + HERO_IMAGES[0] + "')");

    heroContent.add(heroText, heroImageContainer);
    heroSection.add(heroContent);
    self.add(heroSection);
  }

  private void createToolbar() {
    FlexLayout toolbar = new FlexLayout();
    toolbar.addClassName("news-view__toolbar");
    toolbar.setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER)
        .setWrap(FlexWrap.WRAP);

    // Search and filters
    FlexLayout leftSection = new FlexLayout();
    leftSection.addClassName("news-view__toolbar-left");
    leftSection.setAlignment(FlexAlignment.CENTER);

    searchField = new TextField();
    searchField.addClassName("news-view__search");
    searchField.setPlaceholder("Search news articles...");
    searchField.setPrefixComponent(TablerIcon.create("search"));
    searchField.onValueChange(e -> {
      searchTerm = e.getValue();
      loadContent();
    });

    categoryFilter = new ChoiceBox();
    categoryFilter.addClassName("news-view__filter");
    categoryFilter.add(CATEGORY_ALL);
    categoryFilter.add(CATEGORY_BITCOIN);
    categoryFilter.add(CATEGORY_ETHEREUM);
    categoryFilter.add(CATEGORY_DEFI);
    categoryFilter.add(CATEGORY_REGULATION);
    categoryFilter.add(CATEGORY_TECHNOLOGY);
    categoryFilter.setValue(CATEGORY_ALL);
    categoryFilter.onSelect(e -> {
      currentCategory = e.getSelectedItem().getText();
      loadContent();
    });

    timeFilter = new ChoiceBox();
    timeFilter.addClassName("news-view__filter news-view__filter--time");
    timeFilter.add(TIME_TODAY);
    timeFilter.add(TIME_WEEK);
    timeFilter.add(TIME_MONTH);
    timeFilter.add(TIME_ALL);
    timeFilter.setValue(TIME_TODAY);
    timeFilter.onSelect(e -> {
      currentTimeFilter = e.getSelectedItem().getText();
      loadContent();
    });

    leftSection.add(searchField, categoryFilter, timeFilter);

    // Right section - view options
    FlexLayout rightSection = new FlexLayout();
    rightSection.addClassName("news-view__toolbar-right");
    rightSection.setAlignment(FlexAlignment.CENTER);

    Span viewLabel = new Span("View:");
    viewLabel.addClassName("news-view__view-label");

    Button gridViewBtn = new Button("");
    gridViewBtn.addClassName("news-view__view-btn");
    gridViewBtn.setPrefixComponent(TablerIcon.create("grid-dots"));
    gridViewBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);

    Button listViewBtn = new Button("");
    listViewBtn.addClassName("news-view__view-btn");
    listViewBtn.setPrefixComponent(TablerIcon.create("list"));
    listViewBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);

    rightSection.add(viewLabel, gridViewBtn, listViewBtn);

    toolbar.add(leftSection, rightSection);
    self.add(toolbar);
  }

  private void createMainLayout() {
    FlexLayout mainContainer = new FlexLayout();
    mainContainer.addClassName("news-view__main-container");
    mainContainer.setWrap(FlexWrap.WRAP);

    // Main content area (2/3 width)
    mainContentArea = new FlexLayout();
    mainContentArea.addClassName("news-view__main-content");
    mainContentArea.setDirection(FlexDirection.COLUMN);

    // Sidebar area (1/3 width)
    sidebarArea = new FlexLayout();
    sidebarArea.addClassName("news-view__sidebar");
    sidebarArea.setDirection(FlexDirection.COLUMN);

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
    Div trendingContainer = createSectionContainer();

    FlexLayout trendingHeader = new FlexLayout();
    trendingHeader.addClassName("news-view__section-header");
    trendingHeader.setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER);

    FlexLayout titleWithIcon = new FlexLayout();
    titleWithIcon.addClassName("news-view__section-title-icon");
    titleWithIcon.setAlignment(FlexAlignment.CENTER);

    IconButton trendingIcon = new IconButton(TablerIcon.create("trending-up"));
    trendingIcon.addClassName("news-view__trending-icon");
    trendingIcon.setEnabled(false);

    H3 trendingTitle = new H3("Trending Now");
    trendingTitle.addClassName("news-view__section-title");

    titleWithIcon.add(trendingIcon, trendingTitle);

    Anchor viewAllLink = new Anchor("View All");
    viewAllLink.addClassName("news-view__view-all");

    trendingHeader.add(titleWithIcon, viewAllLink);

    FlexLayout trendingGrid = new FlexLayout();
    trendingGrid.addClassName("news-view__grid");
    trendingGrid.setWrap(FlexWrap.WRAP);

    // Add trending articles (smaller cards)
    for (int i = 0; i < Math.min(TRENDING_ARTICLES_LIMIT, allArticles.size()); i++) {
      NewsArticle article = allArticles.get(i);
      Div trendingCard = createTrendingCard(article, i + 1);
      trendingGrid.add(trendingCard);
    }

    trendingContainer.add(trendingHeader, trendingGrid);
    mainContentArea.add(trendingContainer);
  }

  private Div createTrendingCard(NewsArticle article, int rank) {
    Div card = new Div();
    card.addClassName("news-view__card news-view__card--trending");

    FlexLayout cardHeader = new FlexLayout();
    cardHeader.addClassName("news-view__card-header");
    cardHeader.setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.START);

    Span rankBadge = new Span(String.valueOf(rank));
    rankBadge.addClassName("news-view__badge--rank");

    Span timeAgo = new Span(article.getTimeAgo());
    timeAgo.addClassName("news-view__time");

    cardHeader.add(rankBadge, timeAgo);

    H4 title = new H4(article.getTitle());
    title.addClassName("news-view__article-title");

    Span source = new Span(article.getSource());
    source.addClassName("news-view__source");

    card.add(cardHeader, title, source);
    return card;
  }

  private void createMainArticles() {
    Div articlesContainer = createSectionContainer();

    H3 articlesTitle = new H3("Latest Articles");
    articlesTitle.addClassName("news-view__articles-title");

    FlexLayout articlesGrid = new FlexLayout();
    articlesGrid.addClassName("news-view__articles-grid");
    articlesGrid.setDirection(FlexDirection.COLUMN);

    // Filter articles
    List<NewsArticle> filteredArticles = allArticles.stream()
        .filter(article -> matchesCategory(article, currentCategory))
        .filter(article -> matchesSearch(article, searchTerm))
        .limit(MAIN_ARTICLES_LIMIT)
        .collect(Collectors.toList());

    if (filteredArticles.isEmpty()) {
      Div noResults = new Div();
      noResults.addClassName("news-view__no-results");

      IconButton searchIcon = new IconButton(TablerIcon.create("search-off"));
      searchIcon.addClassName("news-view__no-results-icon");

      H4 noResultsTitle = new H4("No articles found");
      noResultsTitle.addClassName("news-view__no-results-title");

      Paragraph noResultsText = new Paragraph("Try adjusting your search criteria or filters");
      noResultsText.addClassName("news-view__no-results-text");

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
    card.addClassName("news-view__article-card");

    FlexLayout cardContent = new FlexLayout();
    cardContent.addClassName("news-view__article-content");
    cardContent.setWrap(FlexWrap.WRAP);

    // Article image
    Div imageContainer = new Div();
    imageContainer.addClassName("news-view__article-image");
    imageContainer.setStyle("background-image",
        "url('" + (article.getImageUrl() != null ? article.getImageUrl() : "") + "')");

    // Article content
    FlexLayout textContent = new FlexLayout();
    textContent.addClassName("news-view__article-text");
    textContent.setDirection(FlexDirection.COLUMN);

    // Meta info
    FlexLayout metaInfo = new FlexLayout();
    metaInfo.addClassName("news-view__article-meta");
    metaInfo.setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER);

    Span source = new Span(article.getSource());
    source.addClassName("news-view__source--meta");

    Span timeAgo = new Span(article.getTimeAgo());
    timeAgo.addClassName("news-view__time");

    metaInfo.add(source, timeAgo);

    // Title
    H4 title = new H4(article.getTitle());
    title.addClassName("news-view__article-title--main");

    // Description
    Paragraph description = new Paragraph(article.getDescription());
    description.addClassName("news-view__article-desc");

    // Read more link
    Anchor readMore = new Anchor("Read more");
    readMore.addClassName("news-view__read-more");
    readMore.setHref(article.getUrl())
        .setTarget(LINK_TARGET_BLANK);

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
    Div marketContainer = createSectionContainer();

    H3 marketTitle = new H3("Market Highlights");
    marketTitle.addClassName("news-view__section-title");

    FlexLayout highlights = new FlexLayout();
    highlights.addClassName("news-view__market-highlights");
    highlights.setDirection(FlexDirection.COLUMN);

    String[][] marketData = {
        { "Bitcoin", "$67,234", "+2.4%", "success" },
        { "Ethereum", "$2,891", "-0.8%", "danger" },
        { "Market Cap", "$2.1T", "+1.2%", "success" }
    };

    for (String[] data : marketData) {
      FlexLayout item = new FlexLayout();
      item.addClassName("news-view__market-item");
      item.setJustifyContent(FlexJustifyContent.BETWEEN)
          .setAlignment(FlexAlignment.CENTER);

      FlexLayout leftSide = new FlexLayout();
      leftSide.setDirection(FlexDirection.COLUMN);

      Span name = new Span(data[0]);
      name.addClassName("news-view__market-name");

      Span price = new Span(data[1]);
      price.addClassName("news-view__market-price");

      leftSide.add(name, price);

      Span change = new Span(data[2]);
      change.addClassName("news-view__market-change");
      if (data[3].equals("success")) {
        change.addClassName("news-view__market-change--success");
      } else {
        change.addClassName("news-view__market-change--danger");
      }

      item.add(leftSide, change);
      highlights.add(item);
    }

    marketContainer.add(marketTitle, highlights);
    sidebarArea.add(marketContainer);
  }

  private void createPopularArticles() {
    Div popularContainer = createSectionContainer();

    H3 popularTitle = new H3("Most Popular");
    popularTitle.addClassName("news-view__section-title");

    FlexLayout popularList = new FlexLayout();
    popularList.addClassName("news-view__popular-list");
    popularList.setDirection(FlexDirection.COLUMN);

    for (int i = 0; i < Math.min(POPULAR_ARTICLES_LIMIT, allArticles.size()); i++) {
      NewsArticle article = allArticles.get(i);

      FlexLayout item = new FlexLayout();
      item.addClassName("news-view__popular-item");

      Span rank = new Span(String.valueOf(i + 1));
      rank.addClassName("news-view__popular-rank");

      FlexLayout content = new FlexLayout();
      content.addClassName("news-view__popular-content");
      content.setDirection(FlexDirection.COLUMN);

      H4 title = new H4(article.getTitle());
      title.addClassName("news-view__popular-item-title");

      Span timeAgo = new Span(article.getTimeAgo());
      timeAgo.addClassName("news-view__time");

      content.add(title, timeAgo);
      item.add(rank, content);

      popularList.add(item);
    }

    popularContainer.add(popularTitle, popularList);
    sidebarArea.add(popularContainer);
  }

  private void createNewsletterSignup() {
    Div newsletterContainer = new Div();
    newsletterContainer.addClassName("news-view__newsletter");

    IconButton emailIcon = new IconButton(TablerIcon.create("mail"));
    emailIcon.addClassName("news-view__newsletter-icon");

    H3 newsletterTitle = new H3("Stay Informed");
    newsletterTitle.addClassName("news-view__newsletter-title");

    Paragraph newsletterText = new Paragraph("Get the latest crypto news delivered to your inbox daily.");
    newsletterText.addClassName("news-view__newsletter-text");

    TextField emailField = new TextField();
    emailField.addClassName("news-view__newsletter-email");
    emailField.setPlaceholder("Enter your email");

    Button subscribeBtn = new Button("Subscribe");
    subscribeBtn.addClassName("news-view__newsletter-btn");
    subscribeBtn.setTheme(ButtonTheme.DEFAULT);

    newsletterContainer.add(emailIcon, newsletterTitle, newsletterText, emailField, subscribeBtn);
    sidebarArea.add(newsletterContainer);
  }

  private boolean matchesCategory(NewsArticle article, String category) {
    if (CATEGORY_ALL.equals(category)) {
      return true;
    }

    String title = article.getTitle().toLowerCase();
    String description = article.getDescription().toLowerCase();
    String content = title + " " + description;

    switch (category) {
      case CATEGORY_BITCOIN:
        return content.contains("bitcoin") || content.contains("btc");
      case CATEGORY_ETHEREUM:
        return content.contains("ethereum") || content.contains("eth");
      case CATEGORY_DEFI:
        return content.contains("defi") || content.contains("decentralized finance") ||
            content.contains("yield") || content.contains("liquidity");
      case CATEGORY_REGULATION:
        return content.contains("regulation") || content.contains("sec") ||
            content.contains("government") || content.contains("compliance");
      case CATEGORY_TECHNOLOGY:
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

  /**
   * Creates a standard section container with consistent styling
   */
  private Div createSectionContainer() {
    Div container = new Div();
    container.addClassName("news-view__section");
    return container;
  }
}