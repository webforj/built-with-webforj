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

@Route("/signin")
@AnonymousAccess
public class LoginView extends Composite<Login> implements DidEnterObserver {
    private final Login self = getBoundComponent();

    public LoginView() {
        self.setAction("/signin");
        whenAttached().thenAccept(c -> self.open());
    }

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