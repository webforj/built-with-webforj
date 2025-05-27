package com.webforj.howdy.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H3;
import com.webforj.component.html.elements.Img;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

/**
 * The `NoData` class represents a UI component intended to display a "No Data"
 * message using a styled layout. It extends the `Composite` class with a base
 * component of type `FlexLayout` to ensure flexible and responsive design.
 *
 * This component centers its content with a vertical alignment and displays an
 * image along with a header text to communicate the absence of data effectively.
 */
public class NoData extends Composite<FlexLayout>  {
  private FlexLayout self = getBoundComponent();

  /**
   * Constructs a `NoData` component for displaying a "No Data" message.
   *
   * This constructor initializes the layout and styles for the `NoData`
   * component using a `FlexLayout`. The layout is configured to center its
   * content both vertically and horizontally with a column alignment.
   * Spacing and margin styles are also set to ensure proper positioning.
   *
   * The constructed component includes an image (with a "No Data" illustration)
   * and a header text ("No Data") to visually convey the absence of data.
   *
   * The internal layout is designed with a maximum width constraint to ensure
   * appropriate scaling across different screen sizes.
   */
  public NoData() {
    self.addClassName("explore-component");
    self.setStyle("margin", "1em auto");
    self.setDirection(FlexDirection.COLUMN);
    self.setAlignment(FlexAlignment.CENTER);
    self.setMaxWidth(300);
    self.setSpacing(".3em");

    Img img = new Img("ws://nodata.svg");
    img.setMaxWidth(250);
    self.add(img, new H3("No Data"));
  }

  /**
   * Sets the visibility of the component.
   *
   * This method updates the visibility of the `NoData` component by delegating
   * the visibility state to its underlying `FlexLayout` component (`self`).
   * When set to `true`, the component becomes visible; when set to `false`,
   * it is hidden.
   *
   * @param visible a boolean value representing the desired visibility state.
   *                If `true`, the component will be visible. If `false`,
   *                the component will be hidden.
   */
  public void setVisible(boolean visible) {
    self.setStyle("display", visible?"block":"none");
  }
}
