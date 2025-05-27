package com.webforj.tictactoe.views;

import com.webforj.component.Composite;
import com.webforj.component.Expanse;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Img;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.tictactoe.core.SoundManager;

@Route("/")
@FrameTitle("Home")
public class HomeView extends Composite<Div> {
  private final Div self = getBoundComponent();

  public HomeView() {
    self.addClassName("view view--home");

    Img logo = new Img("icons://icon-white-512x300.png", "Tic Tac Toe");
    logo.addClassName("logo");

    Button start = new Button("New Game", ButtonTheme.PRIMARY);
    start.setPrefixComponent(FeatherIcon.PLAY_CIRCLE.create());
    start.setExpanse(Expanse.LARGE);
    start.onClick(e -> {
      SoundManager.getCurrent().playClick();
      Router.getCurrent().navigate(StartView.class);
    });

    Button join = new Button("Join Game");
    join.setPrefixComponent(FeatherIcon.SERVER.create());
    join.setExpanse(Expanse.LARGE);
    join.onClick(e -> {
      SoundManager.getCurrent().playClick();
      Router.getCurrent().navigate(JoinView.class);
    });

    Paragraph footer = new Paragraph();
    footer.setHtml(
        "Made with <span style='color:red'>&#10084;</span> for a better web. <a href='https://webforJ.com' target='_blank'>webforJ.com</a>");

    self.add(logo, start, join, footer);
  }
}
