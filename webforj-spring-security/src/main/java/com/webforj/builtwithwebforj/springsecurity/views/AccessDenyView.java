package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.TablerIcon;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

@Route(value = "/access-denied", outlet = MainLayout.class)
@FrameTitle("Access Denied")
@StyleSheet("ws://access-denied.css")
public class AccessDenyView extends Composite<Div> {

  public AccessDenyView() {
    Div container = getBoundComponent();
    container.addClassName("access-denied-container");

    Icon lockIcon = TablerIcon.create("lock-access");
    lockIcon.addClassName("access-denied-icon");

    H1 title = new H1("FORBIDDEN");
    title.addClassName("access-denied-title");

    Paragraph message = new Paragraph("Oops! This page isn't acessible with your login.");
    message.addClassName("access-denied-message");

    Paragraph subMessage = new Paragraph(
        "This page isn't viewable with your current permissions. ");
    subMessage.addClassName("access-denied-submessage");

    container.add(lockIcon, title, message, subMessage);


  }
}
