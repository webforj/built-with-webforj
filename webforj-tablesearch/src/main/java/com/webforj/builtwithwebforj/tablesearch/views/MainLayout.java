package com.webforj.builtwithwebforj.tablesearch.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Strong;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.applayout.AppLayout.DrawerPlacement;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.router.annotation.Route;

/**
 * The app shell. AppLayout keeps the header fixed and scrolls the content area.
 */
@Route
public class MainLayout extends Composite<AppLayout> {

  public MainLayout() {
    AppLayout self = getBoundComponent();
    self.setDrawerPlacement(DrawerPlacement.HIDDEN);

    Toolbar toolbar = new Toolbar();
    toolbar.addToStart(TablerIcon.create("table"));
    toolbar.addToTitle(new Strong("Table Search"));
    self.addToHeader(toolbar);
  }
}
