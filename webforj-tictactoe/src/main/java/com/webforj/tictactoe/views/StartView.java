package com.webforj.tictactoe.views;

import java.util.UUID;

import com.github.javafaker.Faker;
import com.webforj.Page;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.button.event.ButtonClickEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.icons.IconButton;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.toast.Toast;
import com.webforj.environment.namespace.PrivateNamespace;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.ParametersBag;
import com.webforj.tictactoe.core.SoundManager;

@Route
@FrameTitle("Start New Game")
public class StartView extends Composite<Div> {
  private static final String PREFIX = "tictactoe";
  private final String namespaceKey = UUID.randomUUID().toString().replaceAll("[0-9-]", "");
  private final String namespace = String.format("%s.%s", PREFIX, namespaceKey);

  private final Div self = getBoundComponent();
  private final TextField nickname = new TextField("Nickname", "", "Choose a nickname");
  private final TextField gameKey = new TextField("Game Key", namespace);
  private final Button start = new Button("Start Game", ButtonTheme.PRIMARY);
  private final Button back = new Button("Back");
  private final Faker faker = new Faker();

  public StartView() {
    self.addClassName("view view--start");
    setHeader();
    setBody();
    setFooter();
  }

  private void setHeader() {
    self.add(new H1("Start New Game"));
  }

  private void setBody() {
    nickname.setValue(faker.name().firstName());
    nickname.setHelperText("Your nickname will be visible to other players. Must be at least 3 characters.");
    nickname.onValueChange(e -> {
      String value = e.getValue();
      boolean isInvalid = value.isEmpty() || value.length() < 3 || value.contains(" ");
      nickname.setInvalid(isInvalid);
      start.setEnabled(!isInvalid);
    });

    IconButton copy = new IconButton(FeatherIcon.COPY.create());
    copy.onClick(e -> {
      String value = gameKey.getValue();
      String script = String.format("navigator.clipboard.writeText('%s')", value);
      Page.getCurrent().executeJsVoidAsync(script);
      SoundManager.getCurrent().playClick();
      Toast toast = new Toast();
      toast.setTheme(Theme.SUCCESS);

      Button close = new Button(FeatherIcon.X.create());
      close.setTheme(ButtonTheme.DEFAULT);
      close.onClick(ev -> {
        SoundManager.getCurrent().playClick();
        toast.close();
      });

      toast.add(
          new Paragraph("Game key is copied to clipboard."),
          close);
      toast.open();
    });
    gameKey.setSuffixComponent(copy);
    gameKey.setReadOnly(true);
    gameKey.setHelperText("Share this key with the other player to join the game.");

    self.add(nickname, gameKey);
  }

  private void setFooter() {
    start.setPrefixComponent(FeatherIcon.PLAY.create());
    start.onClick(this::handleStart);

    back.setPrefixComponent(FeatherIcon.CHEVRON_LEFT.create());
    back.onClick(e -> {
      SoundManager.getCurrent().playClick();
      Router.getCurrent().navigate(HomeView.class);
    });

    FlexLayout footer = FlexLayout.create(start, back).horizontal().justify().center().build();
    self.add(footer);
  }

  private void handleStart(ButtonClickEvent ev) {
    SoundManager.getCurrent().playClick();
    PrivateNamespace ns = new PrivateNamespace(
        PREFIX, namespaceKey, true);
    Router.getCurrent().navigate(
        GameView.class,
        ParametersBag.of(String.format("key=%s&nickname=%s&action=start", ns.getName(),
            nickname.getValue())));
  }
}
