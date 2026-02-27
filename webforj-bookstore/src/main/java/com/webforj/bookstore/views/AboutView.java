package com.webforj.bookstore.views;

import com.webforj.App;
import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.H2;
import com.webforj.component.html.elements.Paragraph;
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

    /**
     * Constructs the AboutView.
     */
    public AboutView() {
        self.setDirection(FlexDirection.COLUMN);
        self.setAlignment(FlexAlignment.CENTER);
        self.setItemAlignment(FlexAlignment.CENTER);
        self.setSpacing("var(--dwc-space-l)");
        self.setPadding("var(--dwc-space-xl)");
        self.addClassName("about-view");

        // App icon
        Icon appIcon = TablerIcon.create("books");
        appIcon.addClassName("about-view__icon");

        // Title
        H1 title = new H1(App.getApplicationName());
        title.addClassName("about-view__title");

        // Subtitle
        H2 subtitle = new H2("Book Inventory Management System");
        subtitle.addClassName("about-view__subtitle");

        // Description
        Paragraph description = new Paragraph(
                "A modern web application for managing your bookstore inventory. " +
                        "Built with webforJ and Spring Boot.");
        description.addClassName("about-view__description");

        self.add(appIcon, title, subtitle, description);
    }
}
