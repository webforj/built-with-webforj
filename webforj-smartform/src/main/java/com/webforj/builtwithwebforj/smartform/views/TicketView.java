package com.webforj.builtwithwebforj.smartform.views;

import com.webforj.Environment;
import com.webforj.UploadedFile;
import com.webforj.builtwithwebforj.smartform.model.SupportTicket;
import com.webforj.builtwithwebforj.smartform.service.TicketExtractor;
import com.webforj.component.Composite;
import com.webforj.component.Theme;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.card.Card;
import com.webforj.component.field.TextArea;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.spinner.Spinner;
import com.webforj.component.spinner.SpinnerExpanse;
import com.webforj.component.toast.Toast;
import com.webforj.component.upload.Upload;
import com.webforj.data.binding.BindingContext;
import com.webforj.data.binding.annotation.BindingExclude;
import com.webforj.router.annotation.Route;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;

@Route("/")
public class TicketView extends Composite<FlexLayout> {
  private static final String SAMPLE = """
      Hi team,

      since this morning none of our 40 users can log in to the billing portal. \
      They all get "session expired" right after entering the password. \
      Month-end closing is tomorrow, so this is blocking us badly.

      Please call me back as soon as you can.

      Maria Keller
      Finance Lead, Nordwind GmbH
      maria.keller@nordwind.example""";

  // 1. Paste. The text area is not a ticket property, so it is excluded from binding.
  @BindingExclude
  private final TextArea pasted = new TextArea("Paste an email or note");
  @BindingExclude
  private final Upload upload = new Upload();
  private final Button sample = new Button("Use a sample email");
  private final Button fill = new Button("Fill form", ButtonTheme.PRIMARY);
  private final Div thinking = new Div();

  // 2. Review. The field names match the bean properties, so BindingContext.of finds them.
  private final TextField name = new TextField("Name");
  private final TextField email = new TextField("Email");
  private final TextField priority = new TextField("Priority");
  private final TextField summary = new TextField("Summary");
  private final Button save = new Button("Save ticket", ButtonTheme.PRIMARY);
  private final BindingContext<SupportTicket> context =
      BindingContext.of(this, SupportTicket.class, true);

  private final TicketExtractor extractor;

  public TicketView(TicketExtractor extractor) {
    this.extractor = extractor;

    Div columns = new Div(pasteCard(), reviewCard()).addClassName("smartform__columns");
    getBoundComponent().setDirection(FlexDirection.COLUMN).addClassName("smartform")
        .add(header(), columns);

    context.read(new SupportTicket());
    fill.onClick(e -> fillFromText());
    save.onClick(e -> {
      SupportTicket ticket = new SupportTicket();
      if (context.write(ticket).isValid()) {
        // A real app would persist the ticket here.
        Toast.show("Ticket from " + ticket.getName() + " saved", Theme.SUCCESS);
        context.read(new SupportTicket());
        pasted.setValue("");
      }
    });
  }

  private Div header() {
    return new Div(
        new H1("Fill a form from an email"),
        new Paragraph("Upload a file or paste any text. The AI drafts the ticket, you review it, "
            + "and validation decides what can be saved."))
        .addClassName("smartform__header");
  }

  private Card pasteCard() {
    pasted.addClassName("smartform__pasted");
    pasted.setRows(12);
    sample.setPrefixComponent(TablerIcon.create("mail"));
    sample.onClick(e -> pasted.setValue(SAMPLE));
    fill.setPrefixComponent(TablerIcon.create("sparkles"));

    thinking.addClassName("smartform__thinking").setVisible(false);
    thinking.add(new Spinner(SpinnerExpanse.SMALL), new Span("Reading the text..."));

    // Files upload as soon as they are picked or dropped, so the Upload button is not needed.
    upload.setAutoUpload(Upload.AutoUpload.ALWAYS)
        .addFilter("Emails and documents", "*.txt;*.eml;*.md;*.pdf;*.docx");
    upload.setVisible(false, Upload.Part.UPLOAD_BUTTON);
    upload.onUpload(e -> e.getFiles().forEach(this::readFile));

    Div actions = new Div(sample, fill).addClassName("smartform__actions");
    Card card = new Card(upload, pasted, actions, thinking);
    card.addToIcon(TablerIcon.create("clipboard-text"));
    card.addToTitle(new Span("1. Add the text"));
    card.addToCaption(new Span("Drop a file or paste text. It goes to the model only when you click Fill form."));
    return card;
  }

  private Card reviewCard() {
    priority.setPlaceholder("LOW, NORMAL or HIGH");
    save.setPrefixComponent(TablerIcon.create("device-floppy"));

    Div fields = new Div(name, email, priority, summary, save)
        .addClassName("smartform__fields");
    Card card = new Card(fields);
    card.addToIcon(TablerIcon.create("forms"));
    card.addToTitle(new Span("2. Review and save"));
    card.addToCaption(new Span("Fix anything the AI got wrong. Nothing is saved without you."));
    return card;
  }

  /**
   * Puts the text of an uploaded file into the paste box, so the user sees what goes to the model.
   */
  private void readFile(UploadedFile uploaded) {
    Path folder = null;
    try {
      // Each upload gets its own folder, deleted as soon as its text is read.
      folder = Files.createTempDirectory("smartform");
      File saved = uploaded.move(folder.resolve(uploaded.getSanitizedClientName()).toString());
      String text = new TikaDocumentReader(new FileSystemResource(saved)).get().stream()
          .map(Document::getText)
          .collect(Collectors.joining("\n"));
      pasted.setInvalid(false);
      pasted.setValue(text.strip());
    } catch (Exception ex) {
      Toast.show("Could not read " + uploaded.getClientName(), Theme.DANGER);
    } finally {
      delete(folder);
    }
  }

  private static void delete(Path folder) {
    if (folder == null) {
      return;
    }
    try (Stream<Path> paths = Files.walk(folder)) {
      paths.sorted(Comparator.reverseOrder()).forEach(path -> path.toFile().delete());
    } catch (IOException ex) {
      // A leftover temp folder is harmless, the system cleans it up.
    }
  }

  private void fillFromText() {
    String text = pasted.getValue();
    if (text == null || text.isBlank()) {
      showProblem("Paste or upload some text first.");
      return;
    }

    pasted.setInvalid(false);
    fill.setEnabled(false);
    thinking.setVisible(true);
    extractor.extract(text)
        .whenComplete((ticket, error) -> Environment.runLater(() -> {
          fill.setEnabled(true);
          thinking.setVisible(false);
          // The model can also answer with nothing usable, which maps to no ticket.
          if (error != null || ticket == null) {
            showProblem("Could not read that text. Fill the form by hand.");
            return;
          }
          context.read(ticket);
          context.validate();
        }));
  }

  private void showProblem(String message) {
    pasted.setInvalid(true);
    pasted.setInvalidMessage(message);
  }
}
