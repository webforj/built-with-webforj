package com.webforjrest.views;

import com.webforjrest.components.Explore;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("Blank")
public class HomeView extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public HomeView() {
    self.setHeight("100%");
    self.setAlignment(FlexAlignment.CENTER);
    self.add(new Explore());
  }
}
