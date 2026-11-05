package com.webforj.builtwithwebforj.advancedrag.views;

import static com.webforj.testbed.assertion.WebforjAssertions.verifyThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.advancedrag.rag.Trace;
import com.webforj.builtwithwebforj.advancedrag.rag.TraceFixtures;
import com.webforj.builtwithwebforj.advancedrag.service.DocsAssistant;
import com.webforj.component.button.Button;
import com.webforj.component.html.elements.Span;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = AskView.class)
class AskViewTest {

  @MockitoBean
  private DocsAssistant assistant;

  @Test
  void shouldShowTheAnswer(WebforjTestbed context) {
    when(assistant.ask("What does E-4012 mean?"))
        .thenReturn(CompletableFuture.completedFuture(
            new DocsAssistant.Answer("The license token has expired.", new Trace(), 1200)));
    context.navigate(AskView.class);

    UserInteraction.click(ask());

    assertEquals("The license token has expired.", answer().getContent());
    assertTrue(ComponentLocator.of(Span.class).byText("1. Rewrite").isPresent());
    assertTrue(ComponentLocator.of(Span.class).byText("5. Answer").isPresent());
    assertTrue(ComponentLocator.of(Span.class).byText("Searched for: not run").isPresent());
    verifyThat(ask()).isEnabled();
  }

  @Test
  void shouldShowEachStepAndHighlightTheEntryTheAnswerUsed(WebforjTestbed context) {
    when(assistant.ask("What does E-4012 mean?")).thenReturn(CompletableFuture.completedFuture(
        new DocsAssistant.Answer("Expired token.", TraceFixtures.sample(), 3000)));
    context.navigate(AskView.class);

    UserInteraction.click(ask());

    assertTrue(ComponentLocator.of(Span.class).byText("Searched for: error E-4012").isPresent());
    assertTrue(ComponentLocator.of(Span.class).byText("Kept 1 of 2 candidates").isPresent());
    assertTrue(ComponentLocator.of(Span.class).byText("E-4012")
        .byClassName("pipeline__chip--used").isPresent());
    assertTrue(ComponentLocator.of(Span.class).byText("E-4212").isPresent());
    assertFalse(ComponentLocator.of(Span.class).byText("E-4212")
        .byClassName("pipeline__chip--used").isPresent());
  }

  @Test
  void shouldExplainAFailedSearch(WebforjTestbed context) {
    when(assistant.ask("What does E-4012 mean?"))
        .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("quota")));
    context.navigate(AskView.class);

    UserInteraction.click(ask());

    assertEquals("The search failed. Try again.", answer().getContent());
    verifyThat(ask()).isEnabled();
  }

  private static Button ask() {
    return ComponentLocator.of(Button.class).byText("Ask").locate();
  }

  private static MarkdownViewer answer() {
    return ComponentLocator.of(MarkdownViewer.class).locate();
  }
}
