package com.webforj.builtwithwebforj.ui5.views;

import com.webforj.builtwithwebforj.ui5.components.Ui5Toast;
import com.webforj.spring.scope.annotation.EnvironmentScope;
import org.springframework.stereotype.Component;

/**
 * Shows a short message to the user.
 *
 * <p>
 * The toast is one component of the layout, so a message stays visible while the router swaps the
 * view under it. The scope gives every browser tab its own notifier.
 * </p>
 */
@Component
@EnvironmentScope
public class Notifier {
  private final Ui5Toast toast = new Ui5Toast();

  /**
   * Gets the toast, for the layout to place on the page.
   *
   * @return the toast
   */
  Ui5Toast getToast() {
    return toast;
  }

  /**
   * Shows a message.
   *
   * @param message the message
   */
  public void show(String message) {
    toast.show(message);
  }
}
