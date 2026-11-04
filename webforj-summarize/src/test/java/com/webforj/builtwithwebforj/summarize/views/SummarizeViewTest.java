package com.webforj.builtwithwebforj.summarize.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.act.component.upload.UploadInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.summarize.service.SummaryService;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.component.toast.Toast;
import com.webforj.component.upload.Upload;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = SummarizeView.class)
class SummarizeViewTest {

  @MockitoBean
  private SummaryService summaries;

  @Test
  void shouldShowTheSummaryAsMarkdown(WebforjTestbed context) {
    when(summaries.summarize(any(Path.class)))
        .thenReturn(CompletableFuture.completedFuture("- Point one"));

    upload(context);

    assertEquals("- Point one", result().getContent());
  }

  @Test
  void shouldReportTheCauseOfAFailedSummary(WebforjTestbed context) {
    when(summaries.summarize(any(Path.class))).thenReturn(CompletableFuture.failedFuture(
        new CompletionException(new IllegalStateException("quota"))));

    upload(context);

    assertEquals("Could not summarize this document.", result().getContent());
    assertEquals("Summary failed: quota", ComponentLocator.of(Toast.class).locate().getText());
  }

  private static void upload(WebforjTestbed context) {
    context.navigate(SummarizeView.class);
    context.pickFile("notes.txt", "Some notes".getBytes(StandardCharsets.UTF_8));
    UserInteraction.perform(
        ComponentLocator.of(Upload.class).locate(), UploadInteraction.upload("notes.txt"));
  }

  private static MarkdownViewer result() {
    return ComponentLocator.of(MarkdownViewer.class).locate();
  }
}
