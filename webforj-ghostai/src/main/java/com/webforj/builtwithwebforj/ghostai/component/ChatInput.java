package com.webforj.builtwithwebforj.ghostai.component;

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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChatInput extends Composite<Div> implements HasStyle<ChatInput>, HasClassName<ChatInput> {

  public enum State {
    IDLE, STREAMING
  }

  private final Div self = getBoundComponent();
  private final TextArea textArea = new TextArea();
  private final Button actionButton = new Button();
  private ScheduledFuture<?> pendingPrediction;
  private State state = State.IDLE;
  private Consumer<String> sendHandler;
  private Consumer<Void> stopHandler;

  public ChatInput(PredictionService predictionService, ScheduledExecutorService debouncer) {
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
      try {
        if (pendingPrediction != null) {
          pendingPrediction.cancel(false);
        }
      } finally {
        pendingPrediction = null;
      }

      String input = e.getValue();
      if (input == null || input.trim().length() < 10 || state != State.IDLE) {
        textArea.setPredictedText("");
        return;
      }

      pendingPrediction = debouncer.schedule(() -> Environment.runLater(() -> {
        String prediction = predictionService.predict(input);
        if (prediction != null && !prediction.isEmpty()) {
          textArea.setPredictedText(prediction);
        }
      }), 250, TimeUnit.MILLISECONDS);
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
