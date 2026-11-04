package com.webforj.builtwithwebforj.summarize.views;

import com.webforj.Environment;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.card.Card;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.component.toast.Toast;
import com.webforj.component.upload.Upload;
import com.webforj.UploadedFile;
import com.webforj.builtwithwebforj.summarize.service.SummaryService;
import com.webforj.router.annotation.Route;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletionException;

@Route("/")
public class SummarizeView extends Composite<FlexLayout> {
  private final SummaryService summaries;
  private final Path uploads;
  private final MarkdownViewer result = new MarkdownViewer("Upload a document to see its summary.");

  public SummarizeView(SummaryService summaries) {
    this.summaries = summaries;
    this.uploads = createUploadFolder();

    Upload upload = new Upload();
    upload.setSelectionMode(Upload.SelectionMode.SINGLE);
    upload.setAutoUpload(Upload.AutoUpload.ALWAYS);
    upload.addFilter("Documents", "*.pdf;*.docx;*.txt;*.md");
    upload.onUpload(e -> summarize(e.getFile()));

    result.addClassName("summarize__result");
    Card card = new Card(upload, result);
    card.addToIcon(TablerIcon.create("file-text"));
    card.addToTitle(new Span("Your document"));
    card.addToCaption(new Span("PDF, Word, text or Markdown"));

    Div header = new Div(
        new H1("Summarize a document"),
        new Paragraph("Upload a file and the AI sums it up in five bullet points."));

    getBoundComponent().setDirection(FlexDirection.COLUMN).addClassName("summarize")
        .add(header, card);
  }

  private void summarize(UploadedFile uploaded) {
    Path target = uploads.resolve(UUID.randomUUID() + "-" + uploaded.getSanitizedClientName());
    try {
      File saved = uploaded.move(target.toString());
      result.setText("Summarizing " + uploaded.getClientName() + "...");
      summaries.summarize(saved.toPath()).whenComplete((summary, error) ->
          Environment.runLater(() -> showResult(summary, error)));
    } catch (IOException ex) {
      Toast.show("Could not read " + uploaded.getClientName(), Theme.DANGER);
    }
  }

  private void showResult(String summary, Throwable error) {
    if (error != null) {
      // The async proxy can wrap the failure, so report the underlying cause.
      Throwable cause = error instanceof CompletionException && error.getCause() != null
          ? error.getCause() : error;
      result.setText("Could not summarize this document.");
      String reason = cause.getMessage() != null ? cause.getMessage() : "unknown error";
      Toast.show("Summary failed: " + reason, Theme.DANGER);
    } else {
      result.setText(summary);
    }
  }

  private static Path createUploadFolder() {
    try {
      return Files.createTempDirectory("summaries");
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
}
