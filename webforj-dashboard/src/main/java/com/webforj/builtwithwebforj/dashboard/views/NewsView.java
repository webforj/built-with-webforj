package com.webforj.builtwithwebforj.dashboard.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.dashboard.components.news.HeroSection;
import com.webforj.builtwithwebforj.dashboard.components.news.MarketHighlights;
import com.webforj.builtwithwebforj.dashboard.components.news.NewsletterSignup;
import com.webforj.builtwithwebforj.dashboard.components.news.SearchToolbar;
import com.webforj.builtwithwebforj.dashboard.components.news.TrendingSection;
import com.webforj.builtwithwebforj.dashboard.models.NewsArticle;
import com.webforj.builtwithwebforj.dashboard.services.NewsService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
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
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

import java.util.List;

@Route(value = "news", outlet = MainLayout.class)
@StyleSheet("ws://news-view.css")
@FrameTitle("Financial News")
public class NewsView extends Composite<FlexLayout> {

  private static final String LINK_TARGET_BLANK = "_blank";

  private static final int POPULAR_ARTICLES_LIMIT = 4;
  private static final int MAIN_ARTICLES_LIMIT = 6;
  private final FlexLayout self = getBoundComponent();
  private final NewsService newsService;
  private List<NewsArticle> allArticles;
  private FlexLayout mainContentArea;
  private FlexLayout sidebarArea;
  private SearchToolbar searchToolbar;
  private FlexLayout articlesGrid;
  private FlexLayout articlesContainer;

  public NewsView(NewsService newsService) {
    this.newsService = newsService;
    self.addClassName("news-view")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-xl)");

    allArticles = newsService.getMockCryptoNews();
    createHeader();
    createHeroSection();
    createToolbar();
    createMainLayout();
    loadContent();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.addClassName("news-view__header")
        .setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER)
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-m)");

    // Brand section
    FlexLayout brand = new FlexLayout();
    brand.addClassName("news-view__brand")
        .setAlignment(FlexAlignment.CENTER)
        .setSpacing("var(--dwc-space-s)");

    H1 title = new H1("Market News");
    title.addClassName("news-view__title");

    Span badge = new Span("LIVE");
    badge.addClassName("news-view__badge");

    brand.add(title, badge);

    // Action buttons
    FlexLayout actions = new FlexLayout();
    actions.addClassName("news-view__actions")
        .setSpacing("var(--dwc-space-s)");

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
    self.add(new HeroSection());
  }

  private void createToolbar() {
    searchToolbar = new SearchToolbar();
    searchToolbar.setOnSearchCallback(this::performSearch);
    searchToolbar.setOnFilterChangeCallback(this::performSearch);
    self.add(searchToolbar);
  }

  private void createMainLayout() {
    FlexLayout mainContainer = new FlexLayout();
    mainContainer.addClassName("news-view__main-container")
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-xl)");

    // Main content area (2/3 width)
    mainContentArea = new FlexLayout();
    mainContentArea.addClassName("news-view__main-content")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-xl)");

    // Sidebar area (1/3 width)
    sidebarArea = new FlexLayout();
    sidebarArea.addClassName("news-view__sidebar")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-xl)");

    mainContainer.add(mainContentArea, sidebarArea);
    self.add(mainContainer);
  }

  private void loadContent() {
    mainContentArea.removeAll();
    sidebarArea.removeAll();

    createTrendingSection();
    createMainArticles();
    createSidebar();
  }

  private void createTrendingSection() {
    mainContentArea.add(new TrendingSection(allArticles));
  }

  private void createMainArticles() {
    articlesContainer = createSectionContainer();

    H3 articlesTitle = new H3("Latest Articles");
    articlesTitle.addClassName("news-view__articles-title");

    articlesGrid = new FlexLayout();
    articlesGrid.addClassName("news-view__articles-grid")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-l)");

    articlesContainer.add(articlesTitle, articlesGrid);
    mainContentArea.add(articlesContainer);

    // Load initial articles
    updateArticlesGrid();
  }

  private FlexLayout createMainArticleCard(NewsArticle article) {
    FlexLayout card = new FlexLayout();
    card.addClassName("news-view__article-card")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-m)");

    FlexLayout cardContent = new FlexLayout();
    cardContent.addClassName("news-view__article-content")
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-m)");

    // Article image
    Div imageContainer = new Div();
    imageContainer.addClassName("news-view__article-image");
    imageContainer.setStyle("background-image",
        "url('" + (article.getImageUrl() != null ? article.getImageUrl() : "") + "')");

    // Article content
    FlexLayout textContent = new FlexLayout();
    textContent.addClassName("news-view__article-text")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-s)");

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
    sidebarArea.add(new MarketHighlights());
    createPopularArticles();
    sidebarArea.add(new NewsletterSignup());
  }

  private void createPopularArticles() {
    FlexLayout popularContainer = createSectionContainer();

    H3 popularTitle = new H3("Most Popular");
    popularTitle.addClassName("news-view__section-title");

    FlexLayout popularList = new FlexLayout();
    popularList.addClassName("news-view__popular-list")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-m)");

    for (int i = 0; i < Math.min(POPULAR_ARTICLES_LIMIT, allArticles.size()); i++) {
      NewsArticle article = allArticles.get(i);

      FlexLayout item = new FlexLayout();
      item.addClassName("news-view__popular-item")
          .setAlignment(FlexAlignment.START)
          .setSpacing("var(--dwc-space-s)");

      Span rank = new Span(String.valueOf(i + 1));
      rank.addClassName("news-view__popular-rank");

      FlexLayout content = new FlexLayout();
      content.addClassName("news-view__popular-content")
          .setDirection(FlexDirection.COLUMN)
          .setSpacing("var(--dwc-space-xs)");

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

  private boolean matchesCategory(NewsArticle article, String category) {
    if ("All Categories".equals(category)) {
      return true;
    }

    String title = article.getTitle().toLowerCase();
    String description = article.getDescription().toLowerCase();
    String content = title + " " + description;

    return switch (category) {
      case "Bitcoin" -> content.contains("bitcoin") || content.contains("btc");
      case "Ethereum" -> content.contains("ethereum") || content.contains("eth");
      case "DeFi" -> content.contains("defi") || content.contains("decentralized finance") ||
          content.contains("yield") || content.contains("liquidity");
      case "Regulation" -> content.contains("regulation") || content.contains("sec") ||
          content.contains("government") || content.contains("compliance");
      case "Technology" -> content.contains("blockchain") || content.contains("technology") ||
          content.contains("protocol") || content.contains("upgrade");
      default -> true;
    };
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

  private void performSearch() {
    updateArticlesGrid();
  }

  private void updateArticlesGrid() {
    articlesGrid.removeAll();

    // Filter articles
    String currentCategory = searchToolbar.getSelectedCategory();
    String searchTerm = searchToolbar.getSearchTerm();

    List<NewsArticle> filteredArticles = allArticles.stream()
        .filter(article -> matchesCategory(article, currentCategory))
        .filter(article -> matchesSearch(article, searchTerm))
        .limit(MAIN_ARTICLES_LIMIT)
        .toList();

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
        FlexLayout articleCard = createMainArticleCard(article);
        articlesGrid.add(articleCard);
      }
    }
  }

  /**
   * Creates a standard section container with consistent styling
   */
  private FlexLayout createSectionContainer() {
    FlexLayout container = new FlexLayout();
    container.addClassName("news-view__section")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-m)");
    return container;
  }
}