package com.webforj.bookstore.views;

import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.login.Login;
import com.webforj.component.toast.Toast;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.DidEnterEvent;
import com.webforj.router.history.ParametersBag;
import com.webforj.router.observer.DidEnterObserver;
import com.webforj.router.security.annotation.AnonymousAccess;

/**
 * View for user authentication.
 * <p>
 * Displays a login form and handles feedback for authentication errors or
 * logout messages.
 * </p>
 * 
 * @author webforJ Bookstore
 */
@Route("/signin")
@AnonymousAccess
public class LoginView extends Composite<Login> implements DidEnterObserver {
    private final Login self = getBoundComponent();

    /**
     * Constructs the LoginView and configures the login action.
     */
    public LoginView() {
        self.setAction("/signin");
        whenAttached().thenAccept(c -> self.open());
    }

    /**
     * Handles the event when the view is entered, checking for error or logout
     * parameters.
     * 
     * @param event  the enter event
     * @param params the navigation parameters
     */
    @Override
    public void onDidEnter(DidEnterEvent event, ParametersBag params) {
        ParametersBag queryParams = event.getLocation().getQueryParameters();

        if (queryParams.containsKey("error")) {
            Toast.show("Invalid username or password. Please try again.", Theme.DANGER);
        }

        if (queryParams.containsKey("logout")) {
            Toast.show("You have been logged out successfully.", Theme.GRAY);
        }
    }
}