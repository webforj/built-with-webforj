package com.webforj.builtwithwebforj.springsecurity.views;

import com.webforj.annotation.StyleSheet;
import com.webforj.builtwithwebforj.springsecurity.components.SocialLoginButton;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.login.Login;
import com.webforj.component.login.LoginI18n;
import com.webforj.component.login.Login.PasswordMediation;
import com.webforj.component.toast.Toast;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;
import com.webforj.router.security.annotation.AnonymousAccess;

@Route("/signin")
@AnonymousAccess
@StyleSheet("ws://login.css")
public class LoginView extends Composite<Login> implements DidEnterObserver {
  private final Login self = getBoundComponent();

  public LoginView() {
    setupOAuthButtons();

    LoginI18n i18n = self.getI18n();
    i18n.setTitle("Sign In");
    self.setI18n(i18n);
    self.setPasswordMediation(PasswordMediation.SILENT);
    self.setAction("/signin");

    whenAttached().thenAccept(c -> {
      self.open();
    });
  }

  @Override
  public void onDidEnter(DidEnterEvent event, ParametersBag params) {
    ParametersBag queryParams = event.getLocation().getQueryParameters();

    if (queryParams.containsKey("error")) {
      Toast.show("Oops! Something went wrong. Please try again.", Theme.DANGER);
    }

    if (queryParams.containsKey("logout")) {
      Toast.show("You have been logged out successfully.", Theme.GRAY);
    }
  }

  private void setupOAuthButtons() {
    // Header description
    Div headerContainer = new Div();
    headerContainer.addClassName("login-header");
    Paragraph descText = new Paragraph("Choose your preferred sign in method");
    descText.addClassName("login-description");
    headerContainer.add(descText);

    // OAuth container with social login options
    Div oauthContainer = new Div();
    oauthContainer.addClassName("oauth-container");

    SocialLoginButton googleButton = new SocialLoginButton(SocialLoginButton.Provider.GOOGLE);
    SocialLoginButton githubButton = new SocialLoginButton(SocialLoginButton.Provider.GITHUB);

    Paragraph orDivider = new Paragraph("OR");
    orDivider.addClassName("oauth-divider");

    Paragraph emailSignIn = new Paragraph("Sign in with your email or username");
    emailSignIn.addClassName("email-signin-text");

    oauthContainer.add(
        headerContainer, googleButton, githubButton, orDivider, emailSignIn);

    self.addToBeforeForm(oauthContainer);
  }
}
