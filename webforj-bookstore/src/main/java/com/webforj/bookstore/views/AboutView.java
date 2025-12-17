package com.webforj.bookstore.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.Route;

/**
 * View used to display information about the application.
 * 
 */
@Route(value = "/About", outlet = MainLayout.class)
@StyleSheet("ws://about.css")
public class AboutView extends Composite<FlexLayout> {
    private FlexLayout self = getBoundComponent();

    /**
     * Constructs the AboutView.
     */
    public AboutView() {
        self.addClassName("about-view");
        self.setDirection(com.webforj.component.layout.flexlayout.FlexDirection.COLUMN);

        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 15; i++) {
            com.webforj.component.icons.Icon bookIcon = com.webforj.component.icons.FeatherIcon.BOOK.create();
            bookIcon.addClassName("flying-book");

            // Random size between 30px and 80px
            int size = 30 + random.nextInt(51);
            bookIcon.setSize(size + "px", size + "px");

            // Random color
            String color = String.format("#%06x", random.nextInt(0xffffff + 1));
            bookIcon.setStyle("color", color);

            // Random negative delay so they start at different points in the animation
            // The animation duration is 30s, so random delay between -30s and 0s
            int delay = -random.nextInt(30);
            bookIcon.setStyle("animation-delay", delay + "s");

            self.add(bookIcon);
        }

        com.webforj.component.html.elements.H1 title = new com.webforj.component.html.elements.H1("webforJ Bookstore");
        title.addClassName("about-text");

        self.add(title);
    }
}
