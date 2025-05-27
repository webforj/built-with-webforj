package com.webforj.tictactoe.views;

import com.github.javafaker.Faker;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.button.event.ButtonClickEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.icons.FeatherIcon;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.Router;
import com.webforj.router.annotation.FrameTitle;
import com.webforj.router.annotation.Route;
import com.webforj.router.history.ParametersBag;
import com.webforj.tictactoe.core.SoundManager;

@Route
@FrameTitle("Join Game")
public class JoinView extends Composite<Div> {
  private final Div self = getBoundComponent();
  private final TextField nickname = new TextField("Nickname", "", "Choose a nickname");
  private final TextField gameKey = new TextField("Game Key", "", "Enter the game key");
  private final Button join = new Button("Join Game", ButtonTheme.PRIMARY);
  private final Button back = new Button("Back");
  private final Faker faker = new Faker();

  public JoinView() {
    self.addClassName("view view--join");
    setHeader();
    setBody();
    setFooter();
  }

  private void setHeader() {
    self.add(new H1("Join Game"));
  }

  private void setBody() {
    join.setEnabled(false);

    nickname.setValue(faker.name().firstName());
    nickname.setHelperText("Your nickname will be visible to other players. Must be at least 3 characters.");
    nickname.onValueChange(e -> validateInputs());
    gameKey.setHelperText("Enter the share code provided by the game host.");
    gameKey.onValueChange(e -> validateInputs());

    self.add(nickname, gameKey);
  }

  private void setFooter() {
    join.setPrefixComponent(FeatherIcon.PLAY.create());
    join.onClick(this::handleJoin);

    back.setPrefixComponent(FeatherIcon.CHEVRON_LEFT.create());
    back.onClick(e -> {
      SoundManager.getCurrent().playClick();
      Router.getCurrent().navigate(HomeView.class);
    });

    FlexLayout footer = FlexLayout.create(join, back).horizontal().justify().center().build();
    self.add(footer);
  }

  private void validateInputs() {
    String nicknameValue = this.nickname.getValue();
    String gameKeyValue = this.gameKey.getValue();
    boolean isNicknameInvalid = nicknameValue.isEmpty() || nicknameValue.length() < 3 || nicknameValue.contains(" ");
    boolean isShareCodeInvalid = gameKeyValue.isEmpty();
    nickname.setInvalid(isNicknameInvalid);
    gameKey.setInvalid(isShareCodeInvalid);
    join.setEnabled(!isNicknameInvalid && !isShareCodeInvalid);
  }

  private void handleJoin(ButtonClickEvent ev) {
    SoundManager.getCurrent().playClick();

    String code = gameKey.getValue();
    Router.getCurrent().navigate(
        GameView.class,
        ParametersBag.of(String.format(
            "key=%s&nickname=%s&action=join",
            code,
            nickname.getValue())));
  }
}
