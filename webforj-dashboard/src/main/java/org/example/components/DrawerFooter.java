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
    self.setDirection(FlexDirection.COLUMN);
    self.setStyle("padding", "var(--dwc-space-m)");
    self.setStyle("border-top", "1px solid var(--dwc-color-gray-90)");
    
    // Add user profile section
    self.add(createUserSection());
    
    // Add quick actions
    self.add(createQuickActions());
    
    // Add version info
    self.add(createVersionInfo());
  }
  
  private FlexLayout createUserSection() {
    FlexLayout userSection = new FlexLayout();
    userSection.setAlignment(FlexAlignment.CENTER);
    userSection.setStyle("gap", "var(--dwc-space-s)");
    userSection.setStyle("margin-bottom", "var(--dwc-space-m)");
    
    // User avatar
    Div avatar = new Div();
    avatar.setStyle("width", "32px");
    avatar.setStyle("height", "32px");
    avatar.setStyle("border-radius", "50%");
    avatar.setStyle("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)");
    avatar.setStyle("display", "flex");
    avatar.setStyle("align-items", "center");
    avatar.setStyle("justify-content", "center");
    avatar.setStyle("color", "white");
    avatar.setStyle("font-weight", "600");
    avatar.setHtml("<span>JD</span>");
    
    // User info
    FlexLayout userInfo = new FlexLayout();
    userInfo.setDirection(FlexDirection.COLUMN);
    userInfo.setStyle("flex", "1");
    
    Span userName = new Span("John Doe");
    userName.setStyle("font-size", "var(--dwc-font-size-s)");
    userName.setStyle("font-weight", "600");
    userName.setStyle("color", "var(--dwc-color-on-surface)");
    
    Span userRole = new Span("Premium Member");
    userRole.setStyle("font-size", "var(--dwc-font-size-xs)");
    userRole.setStyle("color", "var(--dwc-color-gray-40)");
    
    userInfo.add(userName, userRole);
    
    // Settings icon button
    IconButton userSettings = new IconButton(TablerIcon.create("dots-vertical"));
    userSettings.setStyle("margin-left", "auto");
    
    userSection.add(avatar, userInfo, userSettings);
    
    return userSection;
  }
  
  private FlexLayout createQuickActions() {
    FlexLayout quickActions = new FlexLayout();
    quickActions.setStyle("gap", "var(--dwc-space-xs)");
    quickActions.setStyle("margin-bottom", "var(--dwc-space-m)");
    
    Button helpBtn = new Button("");
    helpBtn.setPrefixComponent(TablerIcon.create("help"));
    helpBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    helpBtn.setStyle("flex", "1");
    
    Button notificationsBtn = new Button("");
    notificationsBtn.setPrefixComponent(TablerIcon.create("bell"));
    notificationsBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    notificationsBtn.setStyle("flex", "1");
    
    Button logoutBtn = new Button("");
    logoutBtn.setPrefixComponent(TablerIcon.create("logout"));
    logoutBtn.setTheme(ButtonTheme.OUTLINED_DEFAULT);
    logoutBtn.setStyle("flex", "1");
    
    quickActions.add(helpBtn, notificationsBtn, logoutBtn);
    
    return quickActions;
  }
  
  private Paragraph createVersionInfo() {
    Paragraph version = new Paragraph("v2.4.1 • © 2024 CryptoTracker");
    version.setStyle("font-size", "var(--dwc-font-size-xs)");
    version.setStyle("color", "var(--dwc-color-gray-50)");
    version.setStyle("text-align", "center");
    version.setStyle("margin", "0");
    
    return version;
  }
}