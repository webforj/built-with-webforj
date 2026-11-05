package com.webforj.builtwithwebforj.ghostai.views;

import static com.webforj.testbed.assertion.WebforjAssertions.verifyThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.ghostai.service.ChatService;
import com.webforj.builtwithwebforj.ghostai.service.PredictionService;
import com.webforj.component.button.Button;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextArea;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = ChatView.class)
class ChatViewTest {

  @MockitoBean
  private ChatService chatService;

  @MockitoBean
  private PredictionService predictionService;

  @Test
  void shouldStreamTheAnswerBelowTheQuestion(WebforjTestbed context) {
    when(chatService.stream(anyString(), eq("What is webforJ?")))
        .thenReturn(Flux.just("A Java ", "web framework."));
    context.navigate(ChatView.class);

    ask("What is webforJ?");

    String content = answer().getContent();
    assertTrue(content.contains("What is webforJ?"));
    assertTrue(content.endsWith("A Java web framework."));
  }

  @Test
  void shouldSendWithTheButtonToo(WebforjTestbed context) {
    when(chatService.stream(anyString(), eq("Hello"))).thenReturn(Flux.just("Boo."));
    context.navigate(ChatView.class);

    UserInteraction.type(input(), "Hello");
    UserInteraction.click(actionButton());

    assertTrue(answer().getContent().endsWith("Boo."));
  }

  @Test
  void shouldShowAStreamError(WebforjTestbed context) {
    when(chatService.stream(anyString(), eq("Hello")))
        .thenReturn(Flux.error(new IllegalStateException("quota")));
    context.navigate(ChatView.class);

    ask("Hello");

    assertTrue(answer().getContent().endsWith("*Error: quota*"));
    verifyThat(input()).isEnabled();
  }

  @Test
  void shouldStopAStreamThatNeverEnds(WebforjTestbed context) {
    when(chatService.stream(anyString(), eq("Hello"))).thenReturn(Flux.never());
    context.navigate(ChatView.class);

    ask("Hello");
    verifyThat(input()).isDisabled();

    UserInteraction.click(actionButton());

    verifyThat(input()).isEnabled();
  }

  @Test
  void shouldSuggestACompletionWhileTyping(WebforjTestbed context) {
    when(predictionService.predict("How do I create"))
        .thenReturn(CompletableFuture.completedFuture("How do I create a Button"));
    context.navigate(ChatView.class);

    UserInteraction.type(input(), "How do I create");
    context.runIntervals();

    assertEquals("How do I create a Button", input().getPredictedText());
  }

  private static void ask(String text) {
    UserInteraction.type(input(), text);
    UserInteraction.keypress(input(), KeypressEvent.Key.ENTER);
  }

  private static TextArea input() {
    return ComponentLocator.of(TextArea.class).locate();
  }

  private static Button actionButton() {
    return ComponentLocator.of(Button.class).locate();
  }

  private static MarkdownViewer answer() {
    return ComponentLocator.of(MarkdownViewer.class).locate();
  }
}
