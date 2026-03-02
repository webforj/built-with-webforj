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
 */
@Route(value = "/management", outlet = MainLayout.class)
@FrameTitle("User Management")
@RolesAllowed("ADMIN")
@StyleSheet("ws://management.css")
public class ManagementView extends Composite<FlexLayout> {

    private FlexLayout self = getBoundComponent();

    public ManagementView() {
        self.setDirection(FlexDirection.COLUMN);
        self.setAlignment(FlexAlignment.CENTER);
        self.setJustifyContent(FlexJustifyContent.CENTER);
        self.setPadding("var(--dwc-space-xl)");
        self.setHeight("100%");
        self.addClassName("management-view");

        // Header card
        Div headerCard = new Div();
        headerCard.addClassName("management-card");

        Icon headerIcon = TablerIcon.create("settings");
        headerIcon.addClassName("management-icon");

        H1 title = new H1("Administration");
        title.addClassName("management-title");

        Div subtitle = new Div("Manage your bookstore settings, users, and system configuration.");
        subtitle.addClassName("management-subtitle");

        Div note = new Div("This page and its navigation tab are only visible to users with the admin role.");
        note.addClassName("management-note");

        headerCard.add(headerIcon, title, subtitle, note);

        self.add(headerCard);
    }
}
