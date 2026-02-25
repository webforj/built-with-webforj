package com.webforj.builtwithwebforj.ghostai.views;

import com.webforj.builtwithwebforj.ghostai.component.ChatFooter;
import com.webforj.builtwithwebforj.ghostai.component.ChatHeader;
import com.webforj.builtwithwebforj.ghostai.component.ChatInput;
import com.webforj.builtwithwebforj.ghostai.component.ChatThinkingIndicator;
import com.webforj.builtwithwebforj.ghostai.component.ChatWelcomeMessage;
import com.webforj.builtwithwebforj.ghostai.service.ChatService;
import com.webforj.builtwithwebforj.ghostai.service.PredictionService;
import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.router.annotation.Route;
import java.util.concurrent.ScheduledExecutorService;
import reactor.core.Disposable;

@Route("/")
public class ChatView extends Composite<FlexLayout> {
  private final FlexLayout self = getBoundComponent();
  private final ChatService chatService;
  private final ChatHeader header = new ChatHeader();
  private final ChatWelcomeMessage welcomeMessage = new ChatWelcomeMessage();
  private final MarkdownViewer markdownViewer = new MarkdownViewer();
  private final ChatInput chatInput;
  private final FlexLayout contentArea;
  private ChatThinkingIndicator thinkingIndicator;
  private Disposable currentSubscription;
  private boolean firstChunkReceived;

  public ChatView(ChatService chatService, PredictionService predictionService,
      ScheduledExecutorService debouncerExecutor) {
    this.chatService = chatService;

    self.setDirection(FlexDirection.COLUMN)
        .setStyle("height", "100vh");

    markdownViewer.setAutoScroll(true)
        .setProgressiveRender(true)
        .setVisible(false);
    markdownViewer.addClassName("chat-centered");

    welcomeMessage.addClassName("chat-centered");

    contentArea = FlexLayout.create(welcomeMessage, markdownViewer).vertical().build();
    contentArea.setStyle("overflowY", "auto")
        .setStyle("padding", "var(--dwc-space-xl)")
        .setStyle("alignItems", "center");
    contentArea.setItemGrow(1, welcomeMessage);
    contentArea.setItemGrow(1, markdownViewer);

    chatInput = new ChatInput(predictionService, debouncerExecutor);
    chatInput.onSend(this::handleSend).onStop(this::handleStop);
    chatInput.addClassName("chat-centered");

    header.addClassName("chat-centered");

    ChatFooter footer = new ChatFooter();
    footer.addClassName("chat-centered");

    self.add(header, contentArea, chatInput, footer);
    self.setItemGrow(1, contentArea);
  }

  private void handleSend(String message) {
    // Hide welcome, show header on first send
    if (welcomeMessage.isVisible()) {
      welcomeMessage.setVisible(false);
      header.setVisible(true);
    }

    // Add separator and user message
    if (!markdownViewer.getContent().isEmpty()) {
      markdownViewer.append("\n\n---\n\n");
    }
    markdownViewer.append("<p style=\"text-align:right;color:var(--dwc-color-primary);"
        + "font-weight:500\">" + message + "</p>\n\n");
    markdownViewer.setVisible(true);
    showThinking();
    firstChunkReceived = false;

    // Set input to streaming state
    chatInput.setState(ChatInput.State.STREAMING);

    // Start streaming
    currentSubscription = chatService.stream(message)
        .doOnNext(chunk -> {
          if (!firstChunkReceived) {
            firstChunkReceived = true;
            hideThinking();
          }
          markdownViewer.append(chunk);
        })
        .doOnError(error -> {
          hideThinking();
          markdownViewer.setVisible(true);
          markdownViewer.append("*Error: " + error.getMessage() + "*");
          currentSubscription = null;
          chatInput.setState(ChatInput.State.IDLE);
          chatInput.focusInput();
        })
        .doOnComplete(() -> {
          currentSubscription = null;
          // Wait for progressive rendering to finish
          markdownViewer.whenRenderComplete().thenAccept(v -> {
            chatInput.setState(ChatInput.State.IDLE);
            chatInput.focusInput();
          });
        })
        .subscribe();
  }

  private void handleStop(Void v) {
    if (currentSubscription != null) {
      currentSubscription.dispose();
      currentSubscription = null;
    }

    hideThinking();
    markdownViewer.setVisible(true);
    markdownViewer.stop();

    chatInput.setState(ChatInput.State.IDLE);
    chatInput.focusInput();
  }

  private void showThinking() {
    thinkingIndicator = new ChatThinkingIndicator();
    thinkingIndicator.addClassName("chat-centered");
    contentArea.add(thinkingIndicator);
  }

  private void hideThinking() {
    if (thinkingIndicator != null) {
      contentArea.remove(thinkingIndicator);
      thinkingIndicator = null;
    }
  }
}
