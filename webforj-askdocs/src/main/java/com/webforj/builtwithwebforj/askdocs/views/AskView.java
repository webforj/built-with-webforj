package com.webforj.builtwithwebforj.askdocs.views;

import com.webforj.Environment;
import com.webforj.UploadedFile;
import com.webforj.builtwithwebforj.askdocs.service.KnowledgeBase;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.card.Card;
import com.webforj.component.event.KeypressEvent;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.markdown.MarkdownViewer;
import com.webforj.component.spinner.Spinner;
import com.webforj.component.spinner.SpinnerExpanse;
import com.webforj.component.Theme;
import com.webforj.component.toast.Toast;
import com.webforj.component.upload.Upload;
import com.webforj.router.annotation.Route;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Route("/")
public class AskView extends Composite<FlexLayout> {
  private static final List<String> EXAMPLES = List.of(
      "What is the hotel limit in Berlin?",
      "When do I need approval for a trip?",
      "How long do I have to submit receipts?");

  private final KnowledgeBase kb;
  private final Path uploads;

  // 1. Add documents
  private final Upload upload = new Upload();
  private final Button sample = new Button("Try the sample handbook");
  private final Div documents = new Div();
  private final Paragraph noDocuments = new Paragraph("No documents yet.");

  // 2. Ask a question
  private final TextField question = new TextField();
  private final Button ask = new Button("Ask", ButtonTheme.PRIMARY);
  private final Div examples = new Div();
  private final Paragraph hint = new Paragraph("Add a document first, then ask about it.");
  private final Div thinking = new Div();
  private final MarkdownViewer answer = new MarkdownViewer();
  private final Div sources = new Div();

  public AskView(KnowledgeBase kb) throws IOException {
    this.kb = kb;
    this.uploads = Files.createTempDirectory("askdocs");

    // Documents on the left, questions on the right. They stack on narrow screens.
    Div columns = new Div(documentsCard(), questionCard()).addClassName("askdocs__columns");
    getBoundComponent().setDirection(FlexDirection.COLUMN).addClassName("askdocs")
        .add(header(), columns);
    setReady(false);
  }

  private Div header() {
    Div header = new Div().addClassName("askdocs__header");
    header.add(new H1("Ask your documents"),
        new Paragraph("Add PDF, Word or text files, then ask questions about them. "
            + "Answers come only from your documents, with the files they used."));
    return header;
  }

  private Card documentsCard() {
    upload.setSelectionMode(Upload.SelectionMode.MULTIPLE)
        .setAutoUpload(Upload.AutoUpload.ALWAYS)
        .addFilter("Documents", "*.pdf;*.docx;*.txt;*.md");
    // Files upload as soon as they are picked or dropped, so the Upload button is not needed.
    upload.setVisible(false, Upload.Part.UPLOAD_BUTTON);
    upload.onUpload(e -> e.getFiles().forEach(this::index));

    sample.setPrefixComponent(TablerIcon.create("book"));
    sample.onClick(e -> {
      add(new ClassPathResource("sample/travel-handbook.md"), "travel-handbook.md");
      sample.setVisible(false);
      examples.setVisible(true);
    });

    documents.addClassName("askdocs__chips").setVisible(false);
    noDocuments.addClassName("askdocs__muted");

    Card card = new Card(upload, sample, noDocuments, documents);
    card.addToIcon(TablerIcon.create("files"));
    card.addToTitle(new Span("1. Add documents"));
    card.addToCaption(new Span("Files stay in memory and are gone when the app stops."));
    return card;
  }

  private Card questionCard() {
    question.setPlaceholder("For example: what is the hotel limit in Berlin?");
    question.addClassName("askdocs__question");
    question.onKeypress(e -> {
      if (e.getKeyCode() == KeypressEvent.Key.ENTER) {
        ask();
      }
    });
    ask.setPrefixComponent(TablerIcon.create("sparkles"));
    ask.onClick(e -> ask());

    Div row = new Div(question, ask).addClassName("askdocs__row");

    examples.addClassName("askdocs__chips").setVisible(false);
    for (String text : EXAMPLES) {
      Button example = new Button(text);
      example.addClassName("askdocs__example");
      example.onClick(e -> {
        question.setValue(text);
        ask();
      });
      examples.add(example);
    }

    hint.addClassName("askdocs__muted");
    thinking.addClassName("askdocs__thinking").setVisible(false);
    thinking.add(new Spinner(SpinnerExpanse.SMALL), new Span("Searching your documents..."));
    answer.addClassName("askdocs__answer").setAutoScroll(true).setVisible(false);
    sources.addClassName("askdocs__chips").setVisible(false);

    Card card = new Card(row, examples, hint, thinking, answer, sources);
    card.addToIcon(TablerIcon.create("message-question"));
    card.addToTitle(new Span("2. Ask a question"));
    card.addToCaption(new Span("Press Enter or click Ask."));
    return card;
  }

  // Indexing ----------------------------------------------------------------

  private void index(UploadedFile uploaded) {
    try {
      // Give every upload a unique name so the same file can be added twice.
      String target = uploads.resolve(UUID.randomUUID() + "-" + uploaded.getSanitizedClientName())
          .toString();
      File saved = uploaded.move(target);
      add(new FileSystemResource(saved), uploaded.getClientName());
    } catch (Exception ex) {
      Toast.show("Could not read " + uploaded.getClientName(), Theme.DANGER);
    }
  }

  private void add(Resource document, String name) {
    int chunks = kb.add(document, name);
    noDocuments.setVisible(false);
    documents.setVisible(true);
    documents.add(chip("file-text", name + " · " + chunks + (chunks == 1 ? " chunk" : " chunks")));
    setReady(true);
  }

  // Asking ------------------------------------------------------------------

  private void ask() {
    String text = question.getValue();
    if (text == null || text.isBlank() || !ask.isEnabled()) {
      return;
    }

    // The answer streams in on another thread. Capture the session's Environment now,
    // on the UI thread, and hand every UI change back to it with runLater.
    Environment env = Environment.getCurrent();
    Set<String> used = new LinkedHashSet<>();
    startAnswer();

    kb.ask(text).subscribe(
        response -> {
          used.addAll(KnowledgeBase.sourcesOf(response));
          String chunk = response.chatResponse().getResult().getOutput().getText();
          Environment.runLater(env, () -> showChunk(chunk));
        },
        error -> Environment.runLater(env, () -> {
          showChunk("Sorry, the answer failed: " + error.getMessage());
          finishAnswer(Set.of());
        }),
        () -> Environment.runLater(env, () -> finishAnswer(used)));
  }

  private void startAnswer() {
    ask.setEnabled(false);
    question.setEnabled(false);
    answer.clear();
    answer.setVisible(false);
    sources.removeAll();
    sources.setVisible(false);
    thinking.setVisible(true);
  }

  private void showChunk(String chunk) {
    if (chunk == null || chunk.isEmpty()) {
      return;
    }
    thinking.setVisible(false);
    answer.setVisible(true);
    answer.append(chunk);
  }

  private void finishAnswer(Set<String> used) {
    thinking.setVisible(false);
    answer.setVisible(true);
    if (!used.isEmpty()) {
      sources.add(new Span("Sources").addClassName("askdocs__muted"));
      used.forEach(name -> sources.add(chip("file-text", name)));
      sources.setVisible(true);
    }
    ask.setEnabled(true);
    question.setEnabled(true);
    question.focus();
  }

  // Helpers -----------------------------------------------------------------

  private void setReady(boolean ready) {
    ask.setEnabled(ready);
    question.setEnabled(ready);
    hint.setVisible(!ready);
  }

  private static Span chip(String icon, String text) {
    Span chip = new Span().addClassName("askdocs__chip");
    chip.add(TablerIcon.create(icon), new Span(text));
    return chip;
  }
}
