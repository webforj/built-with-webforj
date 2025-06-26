package org.example.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class DrawerFooter extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public DrawerFooter() {
    initComponent();
  }

  private void initComponent() {
    self.addClassName("drawer-footer");
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("gap", "5px");

    // Add user profile section
    self.add(createUserSection());
  }

  private FlexLayout createUserSection() {
    FlexLayout userSection = new FlexLayout();
    userSection.addClassName("drawer-footer__user-section");
    userSection.setAlignment(FlexAlignment.CENTER);

    // User avatar
    Div avatar = new Div();
    avatar.addClassName("drawer-footer__avatar");
    avatar.setHtml("<span>JD</span>");

    // User info
    FlexLayout userInfo = new FlexLayout();
    userInfo.addClassName("drawer-footer__user-info");
    userInfo.setDirection(FlexDirection.COLUMN);
    userInfo.setStyle("gap", "5px");

    Span userName = new Span("John Doe");
    userName.addClassName("drawer-footer__user-name");

    Span userRole = new Span("Premium Member");
    userRole.addClassName("drawer-footer__user-role");

    userInfo.add(userName, userRole);

    userSection.add(avatar, userInfo);

    return userSection;
  }
}
