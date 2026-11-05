package com.webforj.builtwithwebforj.ghostai.component;

import com.webforj.Debouncer;
import com.webforj.Environment;
import com.webforj.builtwithwebforj.ghostai.service.PredictionService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.concern.HasClassName;
import com.webforj.concern.HasStyle;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextArea;
import com.webforj.component.html.elements.Div;
import com.webforj.component.icons.DwcIcon;
import com.webforj.component.icons.FeatherIcon;
import java.util.function.Consumer;

public class ChatInput extends Composite<Div> implements HasStyle<ChatInput>, HasClassName<ChatInput> {

  public enum State {
    IDLE, STREAMING
  }

  private final Div self = getBoundComponent();
  private final TextArea textArea = new TextArea();
  private final Button actionButton = new Button();
  // Runs on this user's UI thread, so no thread is shared between users.
  private final Debouncer predictionDebouncer = new Debouncer(0.25f);
  private final PredictionService predictionService;
  private State state = State.IDLE;
  // The text as last typed, so a late prediction for older text can be dropped.
  private String lastInput = "";
  private Consumer<String> sendHandler;
  private Consumer<Void> stopHandler;

  public ChatInput(PredictionService predictionService) {
    this.predictionService = predictionService;
    Div wrapper = new Div();
    wrapper.addClassName("chat-input-wrapper");

    textArea.setPredictedText("Tell me something spooky about Java...");
    textArea.setLineWrap(true);
    textArea.addClassName("chat-input-textarea");
    textArea.onKeypress(e -> {
      if (e.getKeyCode().equals(KeypressEvent.Key.ENTER) && !e.isShiftKey() && state == State.IDLE) {
        send();
      }
    });
    textArea.onValueChange(e -> {
      String input = e.getValue();
      lastInput = input == null ? "" : input;
      if (input == null || input.trim().length() < 10 || state != State.IDLE) {
        predictionDebouncer.cancel();
        textArea.setPredictedText("");
        return;
      }
      predictionDebouncer.run(() -> predict(input));
    });

    actionButton.setTheme(ButtonTheme.PRIMARY);
    actionButton.addClassName("chat-input-button");
    actionButton.onClick(e -> {
      if (state == State.IDLE)
        send();
      else
        stop();
    });

    updateButton();
    wrapper.add(textArea, actionButton);
    self.setStyle("padding", "var(--dwc-space-m) var(--dwc-space-l) var(--dwc-space-l)")
        .add(wrapper);
  }

  private void predict(String input) {
    // The prediction arrives on a Spring worker thread, so capture this session's Environment.
    Environment env = Environment.getCurrent();
    predictionService.predict(input).whenComplete((prediction, error) ->
        Environment.runLater(env, () -> {
          // Drop a prediction for text the user has already changed.
          if (textArea.isDestroyed() || state != State.IDLE || !input.equals(lastInput)) {
            return;
          }
          if (error == null && prediction != null && !prediction.isEmpty()) {
            textArea.setPredictedText(prediction);
          }
        }));
  }

  @Override
  protected void onDidDestroy() {
    predictionDebouncer.cancel();
  }

  private void send() {
    String msg = textArea.getValue();
    if (msg != null && !msg.isBlank() && sendHandler != null) {
      sendHandler.accept(msg.trim());
      textArea.setValue("");
    }
  }

  private void stop() {
    if (stopHandler != null)
      stopHandler.accept(null);
  }

  private void updateButton() {
    actionButton.setPrefixComponent(state == State.IDLE
        ? FeatherIcon.ARROW_UP.create()
        : DwcIcon.STOP.create());
    textArea.setEnabled(state == State.IDLE);
  }

  public ChatInput setState(State state) {
    this.state = state;
    updateButton();
    return this;
  }

  public ChatInput onSend(Consumer<String> handler) {
    this.sendHandler = handler;
    return this;
  }

  public ChatInput onStop(Consumer<Void> handler) {
    this.stopHandler = handler;
    return this;
  }

  public ChatInput focusInput() {
    textArea.focus();
    return this;
  }
}
