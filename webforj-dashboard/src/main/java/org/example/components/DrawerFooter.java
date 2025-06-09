package org.example.components;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
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
    
    // Add user profile section
    self.add(createUserSection());
    
    // Add quick actions
    self.add(createQuickActions());
    
    // Add version info
    self.add(createVersionInfo());
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
    
    Span userName = new Span("John Doe");
    userName.addClassName("drawer-footer__user-name");
    
    Span userRole = new Span("Premium Member");
    userRole.addClassName("drawer-footer__user-role");
    
    userInfo.add(userName, userRole);
    
    // Settings icon button
    IconButton userSettings = new IconButton(TablerIcon.create("dots-vertical"));
    userSettings.addClassName("drawer-footer__user-settings");
    
    userSection.add(avatar, userInfo, userSettings);
    
    return userSection;
  }
  
  private FlexLayout createQuickActions() {
    FlexLayout quickActions = new FlexLayout();
    quickActions.addClassName("drawer-footer__quick-actions");
    
    Button helpBtn = new Button("");
    helpBtn.addClassName("drawer-footer__action-button");
    helpBtn.setPrefixComponent(TablerIcon.create("help"));
    helpBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    
    Button notificationsBtn = new Button("");
    notificationsBtn.addClassName("drawer-footer__action-button");
    notificationsBtn.setPrefixComponent(TablerIcon.create("bell"));
    notificationsBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    
    Button logoutBtn = new Button("");
    logoutBtn.addClassName("drawer-footer__action-button");
    logoutBtn.setPrefixComponent(TablerIcon.create("logout"));
    logoutBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    
    quickActions.add(helpBtn, notificationsBtn, logoutBtn);
    
    return quickActions;
  }
  
  private Paragraph createVersionInfo() {
    Paragraph version = new Paragraph("v2.4.1 • © 2024 CryptoTracker");
    version.addClassName("drawer-footer__version-info");
    
    return version;
  }
}