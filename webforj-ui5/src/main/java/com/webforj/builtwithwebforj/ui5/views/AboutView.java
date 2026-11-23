package com.webforj.builtwithwebforj.ui5.views;

import com.webforj.builtwithwebforj.ui5.components.Ui5Text;
import com.webforj.builtwithwebforj.ui5.components.Ui5Title;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

/**
 * Explains what the app shows.
 */
@Route(value = "about", outlet = MainLayout.class)
@FrameTitle("About")
public class AboutView extends Composite<Div> {

  /**
   * Creates the view.
   */
  public AboutView() {
    getBoundComponent().addClassName("page", "page--narrow").add(
        new Ui5Title("A webforJ app in SAP UI5"),
        new Ui5Text("This app shows that webforJ works with the design system a team already "
            + "has. The SAP UI5 Web Components come from npm through the webforJ frontend "
            + "bundler, and each one is wrapped once as a typed Java component."),
        new Ui5Text("Everything on the screen is a UI5 component: the shell bar, the "
            + "navigation, the table, the form fields, the dialog and the toast. webforJ "
            + "provides the routing and the data binding, and a validation error shows up in "
            + "the UI5 error style of its field."));
  }
}
