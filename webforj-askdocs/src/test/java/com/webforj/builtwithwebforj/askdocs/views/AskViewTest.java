package com.webforj.builtwithwebforj.askdocs.views;

import static com.webforj.testbed.assertion.WebforjAssertions.verifyThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.act.component.upload.UploadInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.askdocs.service.KnowledgeBase;
import com.webforj.component.button.Button;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Span;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.component.toast.Toast;
import com.webforj.component.upload.Upload;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.io.Resource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = AskView.class)
class AskViewTest {
  private static final String SAMPLE = "travel-handbook.md";

  @MockitoBean
  private KnowledgeBase kb;

  @Test
  void shouldLockTheQuestionUntilADocumentIsAdded(WebforjTestbed context) {
    context.navigate(AskView.class);

    verifyThat(question()).isDisabled();
    verifyThat(button("Ask")).isDisabled();
  }

  @Test
  void shouldLoadTheSampleHandbook(WebforjTestbed context) {
    openWithSample(context);

    assertTrue(ComponentLocator.of(Span.class).byText(SAMPLE + " · 2 chunks").isPresent());
    verifyThat(button("Try the sample handbook")).isHidden();
    verifyThat(question()).isEnabled();
  }

  @Test
  void shouldIndexAnUploadedFile(WebforjTestbed context) {
    when(kb.add(any(Resource.class), eq("policy.md"))).thenReturn(1);
    context.navigate(AskView.class);
    context.pickFile("policy.md", "# Policy".getBytes(StandardCharsets.UTF_8));

    UserInteraction.perform(
        ComponentLocator.of(Upload.class).locate(), UploadInteraction.upload("policy.md"));

    assertTrue(ComponentLocator.of(Span.class).byText("policy.md · 1 chunk").isPresent());
    verifyThat(question()).isEnabled();
  }

  @Test
  void shouldWarnWhenAnUploadCannotBeRead(WebforjTestbed context) {
    when(kb.add(any(Resource.class), eq("broken.pdf"))).thenThrow(new IllegalStateException());
    context.navigate(AskView.class);
    context.pickFile("broken.pdf", new byte[] {1, 2, 3});

    UserInteraction.perform(
        ComponentLocator.of(Upload.class).locate(), UploadInteraction.upload("broken.pdf"));

    assertEquals("Could not read broken.pdf", ComponentLocator.of(Toast.class).locate().getText());
  }

  @Test
  void shouldStreamTheAnswerAndListItsSources(WebforjTestbed context) {
    when(kb.ask("Hotel limit?")).thenReturn(Flux.just(
        response("The limit ", SAMPLE), response("is 150 EUR.", SAMPLE)));
    openWithSample(context);

    UserInteraction.type(question(), "Hotel limit?");
    UserInteraction.click(button("Ask"));

    assertEquals("The limit is 150 EUR.", answer().getContent());
    assertTrue(ComponentLocator.of(Span.class).byText("Sources").isPresent());
    assertEquals(1, ComponentLocator.of(Span.class).byText(SAMPLE).count());
  }

  @Test
  void shouldAskWhenTheUserPressesEnter(WebforjTestbed context) {
    when(kb.ask("Receipts?")).thenReturn(Flux.just(response("Within 30 days.", SAMPLE)));
    openWithSample(context);

    UserInteraction.type(question(), "Receipts?");
    UserInteraction.keypress(question(), KeypressEvent.Key.ENTER);

    assertEquals("Within 30 days.", answer().getContent());
  }

  @Test
  void shouldExplainAFailedAnswer(WebforjTestbed context) {
    when(kb.ask("Hotel limit?")).thenReturn(Flux.error(new IllegalStateException("quota")));
    openWithSample(context);

    UserInteraction.type(question(), "Hotel limit?");
    UserInteraction.click(button("Ask"));

    assertEquals("Sorry, the answer failed: quota", answer().getContent());
    verifyThat(button("Ask")).isEnabled();
  }

  private void openWithSample(WebforjTestbed context) {
    when(kb.add(any(Resource.class), eq(SAMPLE))).thenReturn(2);
    context.navigate(AskView.class);
    UserInteraction.click(button("Try the sample handbook"));
  }

  private static ChatClientResponse response(String text, String source) {
    Document doc = Document.builder().text("chunk").metadata(Map.of("source", source)).build();
    return ChatClientResponse.builder()
        .chatResponse(new ChatResponse(List.of(new Generation(new AssistantMessage(text)))))
        .context(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT, List.of(doc))
        .build();
  }

  private static TextField question() {
    return ComponentLocator.of(TextField.class).locate();
  }

  private static Button button(String text) {
    return ComponentLocator.of(Button.class).byText(text).locate();
  }

  private static MarkdownViewer answer() {
    return ComponentLocator.of(MarkdownViewer.class).locate();
  }
}
