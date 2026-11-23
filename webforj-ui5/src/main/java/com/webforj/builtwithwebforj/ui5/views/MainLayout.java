package com.webforj.builtwithwebforj.ui5.views;

import com.webforj.builtwithwebforj.ui5.components.Ui5Button;
import com.webforj.builtwithwebforj.ui5.components.Ui5ShellBar;
import com.webforj.builtwithwebforj.ui5.components.Ui5SideNavigation;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.dispatcher.ListenerRegistration;
import com.webforj.router.RouteOutlet;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;
import com.webforj.router.history.Location;

/**
 * The app shell: a UI5 shell bar on top, a UI5 side navigation on the left and the routed view
 * next to it.
 *
 * <p>
 * The layout is a {@link RouteOutlet}, so the router hands it the view of the current route. The
 * key of each navigation item is the path of the route it opens.
 * </p>
 */
@Route
public class MainLayout extends Composite<Div> implements RouteOutlet {
  private final Ui5SideNavigation navigation = new Ui5SideNavigation();
  private final Div content = new Div();
  private final ListenerRegistration<NavigateEvent> navigateListener;

  /**
   * Creates the layout.
   *
   * @param notifier holds the toast the views show their messages with
   */
  public MainLayout(Notifier notifier) {
    Ui5Button menu = new Ui5Button("", Ui5Button.Design.TRANSPARENT).setIcon("menu2")
        .setTooltipText("Toggle navigation");
    menu.onClick(e -> navigation.setCollapsed(!navigation.isCollapsed()));

    Ui5ShellBar shellBar = new Ui5ShellBar("UI5 Customers", "Built with webforJ");
    shellBar.setStartButton(menu);

    navigation.addItem("customers", "Customers", "customer");
    navigation.addItem("about", "About", "hint");
    navigation.onSelect(e -> Router.getCurrent().navigate(new Location(e.getKey())));

    Div body = new Div(navigation, content);
    body.addClassName("shell__body");
    content.addClassName("shell__content");

    getBoundComponent().addClassName("shell").add(shellBar, body, notifier.getToast());
    navigateListener = Router.getCurrent().onNavigate(this::highlightCurrentRoute);
  }

  @Override
  public void showRouteContent(Component component) {
    content.add(component);
  }

  @Override
  public void removeRouteContent(Component component) {
    content.remove(component);
  }

  @Override
  protected void onDidDestroy() {
    navigateListener.remove();
  }

  // The first segment of the path names the section, so "customers/3" highlights "customers".
  private void highlightCurrentRoute(NavigateEvent event) {
    String path = event.getLocation().getSegments().getPath();
    String section = path.replaceFirst("^/", "").split("/")[0];
    navigation.select(section.isEmpty() ? "customers" : section);
  }
}
