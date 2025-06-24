package org.example.views;

import com.webforj.Page;
import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexWrap;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.component.Component;

@Route(value = "about", outlet = MainLayout.class)
@StyleSheet("ws://about-view.css")
@FrameTitle("About webforJ")
public class AboutView extends Composite<FlexLayout> {
  private final FlexLayout self = getBoundComponent();

  public AboutView() {
    self.addClassName("about-view");
    self.setDirection(FlexDirection.COLUMN);

    createHeader();
    createAboutSection();
    createDocumentationSection();
    createGitHubSection();
    createBuiltWithSection();
  }

  private void createHeader() {
    FlexLayout header = new FlexLayout();
    header.addClassName("about-view__header");
    header.setDirection(FlexDirection.COLUMN);
    header.setAlignment(FlexAlignment.CENTER);

    H1 title = new H1("About webforJ");
    title.addClassName("about-view__title");

    Paragraph subtitle = new Paragraph("Everything you need to know about the modern Java web framework");
    subtitle.addClassName("about-view__subtitle");

    header.add(title, subtitle);
    self.add(header);
  }

  private void createAboutSection() {
    FlexLayout section = new FlexLayout();
    section.addClassName("about-view__section-container");
    section.setDirection(FlexDirection.COLUMN);

    H2 sectionTitle = new H2("What is webforJ?");
    sectionTitle.addClassName("about-view__section-title");

    Paragraph description = new Paragraph();
    description.addClassName("about-view__description");
    description.setHtml("""
        webforJ is a powerful Java framework that enables developers to build modern,
        responsive web applications using familiar Java syntax and patterns. With webforJ,
        you can create rich user interfaces without writing JavaScript, HTML, or CSS -
        though you can enhance your applications with these technologies when needed.
        """);

    // Cards container
    FlexLayout cardsGrid = new FlexLayout();
    cardsGrid.addClassName("about-view__cards-grid");
    cardsGrid.setWrap(FlexWrap.WRAP);
    cardsGrid.setJustifyContent(FlexJustifyContent.BETWEEN);

    cardsGrid.add(
        createFeatureCard("Java-First", "Write web applications using pure Java", "code"),
        createFeatureCard("Component-Based", "Reusable UI components with familiar patterns", "components"),
        createFeatureCard("Responsive Design", "Built-in responsive layouts and styling", "device-mobile"),
        createFeatureCard("Modern UI", "Contemporary design with theming support", "palette"),
        createFeatureCard("Easy Deployment", "Deploy anywhere Java runs", "rocket"),
        createFeatureCard("Active Community", "Growing ecosystem and support", "users"));

    section.add(sectionTitle, description, cardsGrid);
    self.add(section);
  }

  private void createBuiltWithSection() {
    FlexLayout section = new FlexLayout();
    section.addClassName("about-view__section-container");
    section.setDirection(FlexDirection.COLUMN);

    H2 sectionTitle = new H2("Built with webforJ");
    sectionTitle.addClassName("about-view__section-title");

    Paragraph description = new Paragraph();
    description.addClassName("about-view__description");
    description.setHtml("""
        Explore real-world applications built with webforJ. This growing collection showcases
        the framework's capabilities through practical examples and production-ready applications.
        """);

    // Current app highlight
    Div highlightCard = new Div();
    highlightCard.addClassName("about-view__highlight-card");

    H3 appTitle = new H3("You're Here! 🎉 Cryptocurrency Dashboard");
    appTitle.addClassName("about-view__app-title");

    Paragraph appDescription = new Paragraph();
    appDescription.addClassName("about-view__app-description");
    appDescription.setHtml("""
        This cryptocurrency dashboard demonstrates webforJ's capabilities for building
        modern, data-driven web applications with real-time updates and responsive design.
        """);

    highlightCard.add(appTitle, appDescription);

    // Apps grid
    FlexLayout appsGrid = new FlexLayout();
    appsGrid.addClassName("about-view__cards-grid");
    appsGrid.setWrap(FlexWrap.WRAP);
    appsGrid.setJustifyContent(FlexJustifyContent.BETWEEN);

    appsGrid.add(
        createAppCard("webforJ Explorer", "Interactive component explorer", "🧭",
            "https://github.com/webforj/built-with-webforj/tree/main/webforj-explorer"),
        createAppCard("Howdy App", "Social networking demo", "👋",
            "https://github.com/webforj/built-with-webforj/tree/main/webforj-howdy"),
        createAppCard("Tic Tac Toe", "Multiplayer game with real-time sync", "🎮",
            "https://github.com/webforj/built-with-webforj/tree/main/webforj-tictactoe"),
        createContributeCard());

    section.add(sectionTitle, description, highlightCard, appsGrid);
    self.add(section);
  }

  private void createDocumentationSection() {
    FlexLayout section = new FlexLayout();
    section.addClassName("about-view__section-container");
    section.setDirection(FlexDirection.COLUMN);

    H2 sectionTitle = new H2("Documentation");
    sectionTitle.addClassName("about-view__section-title");

    Paragraph description = new Paragraph();
    description.addClassName("about-view__description");
    description.setHtml("""
        Comprehensive guides and API references to help you build powerful web applications.
        From getting started tutorials to advanced topics, our documentation has you covered.
        """);

    // Docs grid
    FlexLayout docsGrid = new FlexLayout();
    docsGrid.addClassName("about-view__cards-grid");
    docsGrid.setWrap(FlexWrap.WRAP);
    docsGrid.setJustifyContent(FlexJustifyContent.BETWEEN);

    docsGrid.add(
        createDocCard("Getting Started", "Core concepts and quick start guide", "rocket",
            "https://docs.webforj.com/docs/introduction/getting-started"),
        createDocCard("Components", "UI components and layout systems", "components",
            "https://docs.webforj.com/docs/components/overview"),
        createDocCard("Routing", "Navigation and page management", "route",
            "https://docs.webforj.com/docs/routing/overview"),
        createDocCard("Styling", "Themes, CSS, and visual design", "palette",
            "https://docs.webforj.com/docs/styling/overview"),
        createDocCard("Archetypes", "Project templates and scaffolding", "template",
            "https://docs.webforj.com/docs/building-ui/archetypes/overview"));

    section.add(sectionTitle, description, docsGrid);
    self.add(section);
  }

  private void createGitHubSection() {
    FlexLayout section = new FlexLayout();
    section.addClassName("about-view__section-container");
    section.setDirection(FlexDirection.COLUMN);

    H2 sectionTitle = new H2("GitHub");
    sectionTitle.addClassName("about-view__section-title");

    Paragraph description = new Paragraph();
    description.addClassName("about-view__description");
    description.setHtml("""
        webforJ is open source and community-driven. Join us on GitHub to contribute,
        report issues, or explore the codebase. Together, we're building the future of Java web development.
        """);

    // Repos grid
    FlexLayout reposGrid = new FlexLayout();
    reposGrid.addClassName("about-view__cards-grid");
    reposGrid.setWrap(FlexWrap.WRAP);
    reposGrid.setJustifyContent(FlexJustifyContent.BETWEEN);

    reposGrid.add(
        createRepoCard("webforj/webforj", "Core framework repository",
            "https://github.com/webforj/webforj"),
        createRepoCard("webforj/webforj-archetypes", "Project templates and starters",
            "https://github.com/webforj/webforj-archetypes"),
        createRepoCard("webforj/built-with-webforj", "Example applications",
            "https://github.com/webforj/built-with-webforj"));

    section.add(sectionTitle, description, reposGrid);
    self.add(section);
  }

  private Div createFeatureCard(String title, String description, String iconName) {
    Div card = new Div();
    card.addClassName("about-view__feature-card");

    Div iconContainer = new Div();
    iconContainer.addClassName("about-view__feature-icon");
    iconContainer.setHtml("<dwc-icon name='" + iconName + "'></dwc-icon>");

    H3 cardTitle = new H3(title);
    cardTitle.addClassName("about-view__feature-title");

    Paragraph cardDesc = new Paragraph(description);
    cardDesc.addClassName("about-view__feature-description");

    card.add(iconContainer, cardTitle, cardDesc);
    return card;
  }

  private Component createAppCard(String title, String description, String icon, String url) {
    Div card = new Div();
    card.addClassName("about-view__app-card", "about-view__clickable-card");
    card.onClick(e -> Page.getCurrent().open(url, "_blank"));

    Div iconDiv = new Div();
    iconDiv.addClassName("about-view__app-icon");
    iconDiv.setText(icon);

    H3 cardTitle = new H3(title);
    cardTitle.addClassName("about-view__card-title");

    Paragraph cardDesc = new Paragraph(description);
    cardDesc.addClassName("about-view__card-description");

    Div linkIcon = new Div();
    linkIcon.addClassName("about-view__external-icon");
    linkIcon.setHtml("<dwc-icon name='external-link'></dwc-icon>");

    card.add(iconDiv, cardTitle, cardDesc, linkIcon);
    return card;
  }

  private Component createContributeCard() {
    Div card = new Div();
    card.addClassName("about-view__app-card", "about-view__clickable-card");
    card.onClick(e -> Page.getCurrent().open("https://github.com/webforj/built-with-webforj", "_blank"));

    Div iconDiv = new Div();
    iconDiv.addClassName("about-view__app-icon");
    iconDiv.setText("➕");

    H3 cardTitle = new H3("Contribute Your App");
    cardTitle.addClassName("about-view__card-title");

    Paragraph cardDesc = new Paragraph("Share your webforJ application with the community");
    cardDesc.addClassName("about-view__card-description");

    Div linkIcon = new Div();
    linkIcon.addClassName("about-view__external-icon");
    linkIcon.setHtml("<dwc-icon name='external-link'></dwc-icon>");

    card.add(iconDiv, cardTitle, cardDesc, linkIcon);
    return card;
  }

  private Div createDocCard(String title, String description, String iconName, String url) {
    Div card = new Div();
    card.addClassName("about-view__feature-card", "about-view__clickable-card");
    card.onClick(e -> Page.getCurrent().open(url, "_blank"));

    Div iconContainer = new Div();
    iconContainer.addClassName("about-view__feature-icon");
    iconContainer.setHtml("<dwc-icon name='" + iconName + "'></dwc-icon>");

    H3 cardTitle = new H3(title);
    cardTitle.addClassName("about-view__feature-title");

    Paragraph cardDesc = new Paragraph(description);
    cardDesc.addClassName("about-view__feature-description");

    Div linkIcon = new Div();
    linkIcon.addClassName("about-view__external-icon");
    linkIcon.setHtml("<dwc-icon name='external-link'></dwc-icon>");

    card.add(iconContainer, cardTitle, cardDesc, linkIcon);
    return card;
  }

  private Component createRepoCard(String title, String description, String url) {
    Div card = new Div();
    card.addClassName("about-view__repo-card", "about-view__clickable-card");
    card.onClick(e -> Page.getCurrent().open(url, "_blank"));

    Div iconContainer = new Div();
    iconContainer.addClassName("about-view__feature-icon");
    iconContainer.setHtml("<dwc-icon name='brand-github'></dwc-icon>");

    H3 cardTitle = new H3(title);
    cardTitle.addClassName("about-view__card-title");

    Paragraph cardDesc = new Paragraph(description);
    cardDesc.addClassName("about-view__card-description");

    Div linkIcon = new Div();
    linkIcon.addClassName("about-view__external-icon");
    linkIcon.setHtml("<dwc-icon name='external-link'></dwc-icon>");

    card.add(iconContainer, cardTitle, cardDesc, linkIcon);
    return card;
  }

}