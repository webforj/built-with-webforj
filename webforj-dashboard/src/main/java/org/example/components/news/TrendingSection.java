package org.example.components.news;

import org.example.models.NewsArticle;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.H4;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;

import java.util.List;

public class TrendingSection extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private static final int TRENDING_ARTICLES_LIMIT = 3;

  public TrendingSection(List<NewsArticle> articles) {
    self.addClassName("news-view__section")
        .setDirection(FlexDirection.COLUMN);

    createTrendingContent(articles);
  }

  private void createTrendingContent(List<NewsArticle> articles) {
    FlexLayout trendingHeader = new FlexLayout();
    trendingHeader.addClassName("news-view__section-header");
    trendingHeader.setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignment(FlexAlignment.CENTER);

    FlexLayout titleWithIcon = new FlexLayout();
    titleWithIcon.addClassName("news-view__section-title-icon")
        .setAlignment(FlexAlignment.CENTER)
        .setSpacing("var(--dwc-space-s)");

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
    trendingGrid.addClassName("news-view__grid")
        .setWrap(FlexWrap.WRAP)
        .setSpacing("var(--dwc-space-m)");

    // Add trending articles (smaller cards)
    for (int i = 0; i < Math.min(TRENDING_ARTICLES_LIMIT, articles.size()); i++) {
      NewsArticle article = articles.get(i);
      FlexLayout trendingCard = createTrendingCard(article, i + 1);
      trendingGrid.add(trendingCard);
    }

    self.add(trendingHeader, trendingGrid);
  }

  private FlexLayout createTrendingCard(NewsArticle article, int rank) {
    FlexLayout card = new FlexLayout();
    card.addClassName("news-view__card news-view__card--trending")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("var(--dwc-space-m)");

    FlexLayout cardHeader = new FlexLayout();
    cardHeader.addClassName("news-view__card-header")
        .setJustifyContent(FlexJustifyContent.BETWEEN)
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

  public void updateArticles(List<NewsArticle> articles) {
    self.removeAll();
    createTrendingContent(articles);
  }
}