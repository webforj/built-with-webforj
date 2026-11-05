package com.webforj.builtwithwebforj.terminal.views;

import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;

@Route("/")
public class HomeView extends Composite<FlexLayout> {

  private final FlexLayout self = getBoundComponent();
  private final H1 title = new H1("webforJ Terminal Demo");
  private final Paragraph description =
      new Paragraph("Experience the power of terminal applications in the browser!");
  private final Button terminalBtn = new Button("Open Terminal");

  public HomeView() {
    self.addClassName("home");
    self.setDirection(FlexDirection.COLUMN);
    self.setAlignment(FlexAlignment.CENTER);

    terminalBtn.setPrefixComponent(FeatherIcon.TERMINAL.create())
        .setTheme(ButtonTheme.SUCCESS)
        .addClickListener(e -> Router.getCurrent().navigate(TerminalView.class));

    self.add(title, description, terminalBtn);
  }
}
