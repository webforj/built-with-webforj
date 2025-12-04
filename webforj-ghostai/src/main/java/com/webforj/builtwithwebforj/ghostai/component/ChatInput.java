package com.webforj.builtwithwebforj.ghostai.component;

import com.webforj.Debouncer;
import com.webforj.builtwithwebforj.ghostai.service.PredictionService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextArea;
import com.webforj.component.html.elements.Div;
import com.webforj.component.icons.DwcIcon;
import com.webforj.component.icons.FeatherIcon;
import java.util.function.Consumer;

public class ChatInput extends Composite<Div> {

  public enum State {
    IDLE, STREAMING
  }

  private final Div self = getBoundComponent();
  private final TextArea textArea = new TextArea();
  private final Button actionButton = new Button();
  private final Debouncer debounce = new Debouncer(0.25f);
  private State state = State.IDLE;
  private Consumer<String> sendHandler;
  private Consumer<Void> stopHandler;

  public ChatInput(PredictionService predictionService) {
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
      if (input == null || input.trim().length() < 10 || state != State.IDLE) {
        debounce.cancel();
        textArea.setPredictedText("");
        return;
      }

      debounce.run(() -> {
        String prediction = predictionService.predict(input);
        if (prediction != null && !prediction.isEmpty()) {
          textArea.setPredictedText(prediction);
        }
      });
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
