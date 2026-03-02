package com.webforj.bookstore.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.Route;

/**
 * View used to display information about the application.
 */
@Route(value = "/About", outlet = MainLayout.class)
@StyleSheet("ws://about.css")
public class AboutView extends Composite<FlexLayout> {
    private FlexLayout self = getBoundComponent();

    public AboutView() {
        self.setDirection(FlexDirection.COLUMN);
        self.setAlignment(FlexAlignment.CENTER);
        self.setItemAlignment(FlexAlignment.CENTER);
        self.setSpacing("var(--dwc-space-l)");
        self.setPadding("var(--dwc-space-xl)");
        self.addClassName("about-view");

        // Hero card
        Div heroCard = new Div();
        heroCard.addClassName("about-view__card");

        Icon appIcon = TablerIcon.create("books");
        appIcon.addClassName("about-view__icon");

        H1 title = new H1("Bookstore");
        title.addClassName("about-view__title");

        H2 subtitle = new H2("Book Inventory Management System");
        subtitle.addClassName("about-view__subtitle");

        Paragraph description = new Paragraph(
                "A full-stack demo application for managing bookstore inventory, "
                        + "built with webforJ and Spring Boot. Browse, search, and organize "
                        + "books by genre, author, and publisher \u2014 all from a modern web interface.");
        description.addClassName("about-view__description");

        Div techBadges = new Div();
        techBadges.addClassName("about-view__badges");
        techBadges.add(
                createBadge("webforJ 25.11"),
                createBadge("Spring Boot 3.5"),
                createBadge("Java 21"),
                createBadge("H2 Database"));

        heroCard.add(appIcon, title, subtitle, description, techBadges);

        // Resources section
        Div resourcesSection = new Div();
        resourcesSection.addClassName("about-view__resources");

        resourcesSection.add(
                createResourceLink(
                        "book-2",
                        "Documentation",
                        "Guides, tutorials, and API reference",
                        "https://docs.webforj.com"),
                createResourceLink(
                        "brand-github",
                        "GitHub",
                        "Source code and issue tracker",
                        "https://github.com/webforj/webforj"),
                createResourceLink(
                        "package",
                        "Samples",
                        "Example apps built with webforJ",
                        "https://github.com/webforj/built-with-webforj"),
                createResourceLink(
                        "world-www",
                        "Website",
                        "Learn more about webforJ",
                        "https://webforj.com"));

        self.add(heroCard, resourcesSection);
    }

    private Span createBadge(String text) {
        Span badge = new Span(text);
        badge.addClassName("about-view__badge");
        return badge;
    }

    private Div createResourceLink(String iconName, String label, String desc, String url) {
        Anchor link = new Anchor();
        link.setHref(url);
        link.setTarget("_blank");
        link.addClassName("about-view__resource-link");

        Icon icon = TablerIcon.create(iconName);
        icon.addClassName("about-view__resource-icon");

        Span name = new Span(label);
        name.addClassName("about-view__resource-name");

        Span description = new Span(desc);
        description.addClassName("about-view__resource-desc");

        link.add(icon, name, description);

        Div wrapper = new Div();
        wrapper.addClassName("about-view__resource");
        wrapper.add(link);
        return wrapper;
    }
}
