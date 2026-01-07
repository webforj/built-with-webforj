package com.webforj.property.views;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;

/** Main property view serving as the router outlet. */
@Route(value = "/", outlet = MainLayout.class)
@FrameTitle("PropertyView")
public class PropertyView extends Composite<Div> {

  /** Creates the property view. */
  public PropertyView() {}
}
