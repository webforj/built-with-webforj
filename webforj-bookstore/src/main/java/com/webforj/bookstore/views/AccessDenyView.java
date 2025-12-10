package com.webforj.bookstore.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.router.annotation.Route;

@Route(value = "/access-denied", outlet = MainLayout.class)
public class AccessDenyView extends Composite<Div> {
    private final Div self = getBoundComponent();

    public AccessDenyView() {
        Paragraph message = new Paragraph("Oops! This area is VIP only.");
        Paragraph subMessage = new Paragraph(
                "Looks like you tried to sneak into the executive lounge! Either grab better credentials or head back to the public areas where the coffee is free anyway.");

        self.add(message, subMessage);
    }
}