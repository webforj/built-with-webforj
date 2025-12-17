package com.webforj.bookstore.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import jakarta.annotation.security.RolesAllowed;

/**
 * View for managing users, restricted to administrators.
 * 
 * @author webforJ Bookstore
 */
@Route(value = "/management", outlet = MainLayout.class)
@FrameTitle("User Management")
@RolesAllowed("ADMIN")
public class ManagementView extends Composite<FlexLayout> {

    private FlexLayout self = getBoundComponent();

    public ManagementView() {
        self.setDirection(FlexDirection.COLUMN);
        self.setPadding("20px");

        H1 title = new H1("User Management");
        Div content = new Div("Restricted Area: Only administrators can see this.");

        self.add(title, content);
    }
}
