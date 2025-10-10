package com.webforjrest.views;

import java.util.Set;

import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.applayout.AppLayout.DrawerPlacement;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;

@Route
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private H1 title = new H1();

  public MainLayout() {
    setHeader();
    Router.getCurrent().onNavigate(this::onNavigate);
  }

  private void setHeader() {
    self.setDrawerPlacement(DrawerPlacement.HIDDEN);

    Toolbar toolbar = new Toolbar();
    toolbar.addToTitle(title);

    self.addToHeader(toolbar);
  }

  private void onNavigate(NavigateEvent ev) {
    Set<Component> components = ev.getContext().getAllComponents();
    Component view = components.stream().filter(c -> c.getClass().getSimpleName().endsWith("View")).findFirst()
        .orElse(null);

    if (view != null) {
      FrameTitle frameTitle = view.getClass().getAnnotation(FrameTitle.class);
      title.setText(frameTitle != null ? frameTitle.value() : "");
    }
  }
}
