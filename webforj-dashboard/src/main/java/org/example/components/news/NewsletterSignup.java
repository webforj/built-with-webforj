package org.example.components.news;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.flexlayout.FlexDirection;

public class NewsletterSignup extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private Div headerSection;
  private Div formSection;

  public NewsletterSignup() {
    self.addClassName("news-view__newsletter")
        .setDirection(FlexDirection.COLUMN);

    createNewsletterContent();
    applyThemeAwareBackground();
  }

  private void createNewsletterContent() {
    // Create header section (blue gradient background)
    headerSection = new Div();
    headerSection.addClassName("news-view__newsletter-header");

    IconButton emailIcon = new IconButton(TablerIcon.create("mail"));
    emailIcon.addClassName("news-view__newsletter-icon");

    H3 newsletterTitle = new H3("Stay Informed");
    newsletterTitle.addClassName("news-view__newsletter-title");

    Paragraph newsletterText = new Paragraph("Get the latest crypto news delivered to your inbox daily.");
    newsletterText.addClassName("news-view__newsletter-text");

    headerSection.add(emailIcon, newsletterTitle, newsletterText);

    // Create form section (surface-2 background)
    formSection = new Div();
    formSection.addClassName("news-view__newsletter-form");

    TextField emailField = new TextField();
    emailField.addClassName("news-view__newsletter-email");
    emailField.setPlaceholder("Enter your email");

    Button subscribeBtn = new Button("Subscribe");
    subscribeBtn.addClassName("news-view__newsletter-btn");
    subscribeBtn.setTheme(ButtonTheme.PRIMARY);

    formSection.add(emailField, subscribeBtn);

    // Add both sections to the container
    self.add(headerSection, formSection);
  }

  private void applyThemeAwareBackground() {
    String theme = com.webforj.App.getTheme();
    String backgroundGradient;
    String textColor;

    if ("dark".equals(theme)) {
      // Dark theme - lighter blue for better visibility
      backgroundGradient = "linear-gradient(135deg, var(--dwc-color-primary-40) 0%, var(--dwc-color-primary-50) 100%)";
      textColor = "var(--dwc-color-on-primary)";
    } else {
      // Light theme - darker blue for better contrast
      backgroundGradient = "linear-gradient(135deg, var(--dwc-color-primary-60) 0%, var(--dwc-color-primary-70) 100%)";
      textColor = "white";
    }

    // Apply gradient only to header section
    headerSection.setStyle("background", backgroundGradient);
    headerSection.setStyle("color", textColor);
  }

  public void updateTheme() {
    applyThemeAwareBackground();
  }
}