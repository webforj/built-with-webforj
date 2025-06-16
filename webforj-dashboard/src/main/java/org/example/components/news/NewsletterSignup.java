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

public class NewsletterSignup extends Composite<Div> {
  private Div self = getBoundComponent();

  public NewsletterSignup() {
    self.addClassName("news-view__newsletter");
    
    createNewsletterContent();
    applyThemeAwareBackground();
  }

  private void createNewsletterContent() {
    IconButton emailIcon = new IconButton(TablerIcon.create("mail"));
    emailIcon.addClassName("news-view__newsletter-icon");

    H3 newsletterTitle = new H3("Stay Informed");
    newsletterTitle.addClassName("news-view__newsletter-title");

    Paragraph newsletterText = new Paragraph("Get the latest crypto news delivered to your inbox daily.");
    newsletterText.addClassName("news-view__newsletter-text");

    TextField emailField = new TextField();
    emailField.addClassName("news-view__newsletter-email");
    emailField.setPlaceholder("Enter your email");

    Button subscribeBtn = new Button("Subscribe");
    subscribeBtn.addClassName("news-view__newsletter-btn");
    subscribeBtn.setTheme(ButtonTheme.DEFAULT);

    self.add(emailIcon, newsletterTitle, newsletterText, emailField, subscribeBtn);
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
    
    self.setStyle("background", backgroundGradient);
    self.setStyle("color", textColor);
  }

  public void updateTheme() {
    applyThemeAwareBackground();
  }
}