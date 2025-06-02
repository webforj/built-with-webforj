package org.example.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Anchor;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class NewsCard extends Composite<FlexLayout> {
    private FlexLayout self = getBoundComponent();

    public NewsCard(String title, String description, String source, String timeAgo, String url) {
        // Title
        H3 titleElement = new H3(title);
        titleElement.setStyle("margin", "0")
                .setStyle("font-size", "var(--dwc-font-size-l)")
                .setStyle("font-weight", "500")
                .setStyle("line-height", "1.3")
                .setStyle("color", "var(--dwc-color-on-surface)");

        // Description
        Paragraph descElement = new Paragraph(description);
        descElement.setStyle("margin", "var(--dwc-space-xs) 0")
                .setStyle("font-size", "var(--dwc-font-size-s)")
                .setStyle("color", "var(--dwc-color-gray-15)")
                .setStyle("line-height", "1.5");

        // Source and time
        Div metaInfo = new Div();
        metaInfo.setStyle("display", "flex")
                .setStyle("gap", "var(--dwc-space-xs)")
                .setStyle("align-items", "center")
                .setStyle("font-size", "var(--dwc-font-size-xs)")
                .setStyle("color", "var(--dwc-color-gray-15)");

        Span sourceSpan = new Span(source);
        sourceSpan.setStyle("font-weight", "500");

        Span separator = new Span("•");
        separator.setStyle("color", "var(--dwc-color-gray-10)");

        Span timeSpan = new Span(timeAgo);

        metaInfo.add(sourceSpan, separator, timeSpan);

        // Read more link
        Anchor readMore = new Anchor(url, "Read more →");
        readMore.setStyle("font-size", "var(--dwc-font-size-s)")
                .setStyle("color", "var(--dwc-color-info-40)")
                .setStyle("text-decoration", "none")
                .setStyle("font-weight", "500")
                .setStyle("margin-top", "var(--dwc-space-xs)")
                .setTarget("_blank");

        // Configure card layout
        self.setDirection(FlexDirection.COLUMN);
        self.addClassName("news-card");

        // Add all elements
        self.add(titleElement, descElement, metaInfo, readMore);
    }
}