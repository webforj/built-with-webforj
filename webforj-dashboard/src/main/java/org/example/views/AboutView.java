package org.example.views;

import com.webforj.Page;
import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.ListEntry;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.UnorderedList;
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
    header.addClassName("about-view__header about-view__primary-gradient");
    header.setDirection(FlexDirection.COLUMN);

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

    UnorderedList featuresList = new UnorderedList();
    featuresList.addClassName("about-view__list");
    
    ListEntry item1 = new ListEntry();
    item1.setHtml("<strong>Java-First:</strong> Write web applications using pure Java");
    
    ListEntry item2 = new ListEntry();
    item2.setHtml("<strong>Component-Based:</strong> Reusable UI components with familiar patterns");
    
    ListEntry item3 = new ListEntry();
    item3.setHtml("<strong>Responsive Design:</strong> Built-in responsive layouts and styling");
    
    ListEntry item4 = new ListEntry();
    item4.setHtml("<strong>Modern UI:</strong> Contemporary design with theming support");
    
    ListEntry item5 = new ListEntry();
    item5.setHtml("<strong>Easy Deployment:</strong> Deploy anywhere Java runs");
    
    ListEntry item6 = new ListEntry();
    item6.setHtml("<strong>Active Community:</strong> Growing ecosystem and support");
    
    featuresList.add(item1, item2, item3, item4, item5, item6);

    section.add(sectionTitle, description, featuresList);
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
    highlightCard.addClassName("about-view__highlight-card about-view__primary-gradient");

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

    UnorderedList docsList = new UnorderedList();
    docsList.addClassName("about-view__list");
    
    ListEntry doc1 = new ListEntry();
    doc1.setHtml("<a href='https://docs.webforj.com/docs/introduction/getting-started' target='_blank'>Getting Started</a> - Core concepts and quick start guide");
    
    ListEntry doc2 = new ListEntry();
    doc2.setHtml("<a href='https://docs.webforj.com/docs/components/overview' target='_blank'>Components</a> - UI components and layout systems");
    
    ListEntry doc3 = new ListEntry();
    doc3.setHtml("<a href='https://docs.webforj.com/docs/routing/overview' target='_blank'>Routing</a> - Navigation and page management");
    
    ListEntry doc4 = new ListEntry();
    doc4.setHtml("<a href='https://docs.webforj.com/docs/styling/overview' target='_blank'>Styling</a> - Themes, CSS, and visual design");
    
    ListEntry doc5 = new ListEntry();
    doc5.setHtml("<a href='https://docs.webforj.com/docs/building-ui/archetypes/overview' target='_blank'>Archetypes</a> - Project templates and scaffolding");
    
    docsList.add(doc1, doc2, doc3, doc4, doc5);

    section.add(sectionTitle, description, docsList);
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

    UnorderedList reposList = new UnorderedList();
    reposList.addClassName("about-view__list");
    
    ListEntry repo1 = new ListEntry();
    repo1.setHtml("<a href='https://github.com/webforj/webforj' target='_blank'>webforj/webforj</a> - Core framework repository");
    
    ListEntry repo2 = new ListEntry();
    repo2.setHtml("<a href='https://github.com/webforj/webforj-archetypes' target='_blank'>webforj/webforj-archetypes</a> - Project templates and starters");
    
    ListEntry repo3 = new ListEntry();
    repo3.setHtml("<a href='https://github.com/webforj/built-with-webforj' target='_blank'>webforj/built-with-webforj</a> - Example applications");
    
    reposList.add(repo1, repo2, repo3);

    section.add(sectionTitle, description, reposList);
    self.add(section);
  }


  private Component createAppCard(String title, String description, String icon, String url) {
    Div card = new Div();
    card.addClassName("about-view__card about-view__app-card about-view__card--clickable");
    card.onClick(e -> Page.getCurrent().open(url, "_blank"));

    Div iconDiv = new Div();
    iconDiv.addClassName("about-view__icon about-view__primary-gradient");
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
    card.addClassName("about-view__card about-view__app-card about-view__card--clickable");
    card.onClick(e -> Page.getCurrent().open("https://github.com/webforj/built-with-webforj", "_blank"));

    Div iconDiv = new Div();
    iconDiv.addClassName("about-view__icon about-view__primary-gradient");
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



}