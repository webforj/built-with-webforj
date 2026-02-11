package com.webforj.bookstore.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.annotation.StyleSheet;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * View for managing users, restricted to administrators.
 * Displays a centered card with a lock icon and restricted access message.
 */
@Route(value = "/management", outlet = MainLayout.class)
@FrameTitle("User Management")
@RolesAllowed("ADMIN")
@StyleSheet("ws://management.css")
public class ManagementView extends Composite<FlexLayout> {

    private FlexLayout self = getBoundComponent();

    public ManagementView() {
        // Main container with design tokens
        self.setDirection(FlexDirection.COLUMN);
        self.setAlignment(FlexAlignment.CENTER);
        self.setJustifyContent(FlexJustifyContent.CENTER);
        self.setSpacing("var(--dwc-space-xl)");
        // Use 100% height to fill available space (after toolbar)
        self.setHeight("100%");
        self.setStyle("box-sizing", "border-box");

        // Icon with larger size and danger color
        Icon lockIcon = TablerIcon.create("lock-access");
        lockIcon.setAttribute("size", "80");
        lockIcon.addClassName("management-lock-icon");

        // Title with proper styling
        H1 title = new H1("User Management");
        title.addClassName("management-title");

        // Description text
        Div description = new Div("This area is restricted to administrators only.");
        description.addClassName("management-description");

        // Content card wrapper
        Div contentCard = new Div();
        contentCard.addClassName("management-card");
        contentCard.add(lockIcon, title, description);

        self.add(contentCard);
    }
}
