package com.webforj.builtwithwebforj.invoicereader.views;

import com.webforj.Environment;
import com.webforj.UploadedFile;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.card.Card;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.H1;
import com.webforj.component.html.elements.Img;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.html.elements.Span;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.upload.Upload;
import com.webforj.router.annotation.Route;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Route("/")
public class InvoiceUploadView extends Composite<FlexLayout> {
  private static final String PROMPT =
      "Extract the vendor, invoice number, invoice date, currency, and total from this invoice. "
          + "Use null for any field you cannot read.";
  private static final String SAMPLE = "sample-invoice.png";

  record InvoiceData(
      String vendor,
      String invoiceNumber,
      String invoiceDate,
      String currency,
      BigDecimal total) {}

  private final FlexLayout self = getBoundComponent();
  private final Upload upload = new Upload();
  private final Paragraph status = new Paragraph("Upload an invoice image to read its fields.");
  private final Paragraph result = new Paragraph();
  private final Button sample = new Button("Try a sample invoice");
  private final Img preview = new Img();
  private final ChatClient chat;

  public InvoiceUploadView(ChatClient chat) {
    this.chat = chat;

    upload.setSelectionMode(Upload.SelectionMode.SINGLE);
    upload.addFilter("Invoice images", "*.png;*.jpg;*.jpeg");
    upload.setMaxFileSize(10d * 1024d * 1024d);
    upload.setAutoUpload(Upload.AutoUpload.ALWAYS);
    upload.setVisible(false, Upload.Part.UPLOAD_BUTTON);
    upload.onUpload(e -> extract(e.getFile()));

    sample.setPrefixComponent(TablerIcon.create("receipt"));
    sample.onClick(e -> extractSample());

    preview.setAlt("Invoice preview").addClassName("invoicereader__preview").setVisible(false);

    Card card = new Card(upload, sample, preview, status, result);
    card.addToIcon(TablerIcon.create("file-invoice"));
    card.addToTitle(new Span("Your invoice"));
    card.addToCaption(new Span("PNG or JPEG, up to 10 MB"));

    Div header = new Div(
        new H1("Read an invoice image"),
        new Paragraph("Upload a photo or scan of an invoice and the AI returns its fields."));

    self.setDirection(FlexDirection.COLUMN).addClassName("invoicereader").add(header, card);
  }

  private void extract(UploadedFile uploaded) {
    status.setText("Reading " + uploaded.getSanitizedClientName() + "...");
    result.setText("");

    // move() needs the session's environment, so it runs here and not on the worker thread.
    File file;
    try {
      file = uploaded.move();
    } catch (IOException ex) {
      status.setText("Could not read " + uploaded.getSanitizedClientName());
      return;
    }
    MimeType type = uploaded.getClientExtension().equalsIgnoreCase("png")
        ? MimeTypeUtils.IMAGE_PNG
        : MimeTypeUtils.IMAGE_JPEG;

    read(file, type);
  }

  // Copies the bundled sample invoice to a temporary file, so it takes the same path as an upload.
  private void extractSample() {
    status.setText("Reading " + SAMPLE + "...");
    result.setText("");

    try (InputStream in = getClass().getResourceAsStream("/invoices/" + SAMPLE)) {
      if (in == null) {
        throw new IOException("missing " + SAMPLE);
      }
      Path copy = Files.createTempFile("invoice", ".png");
      Files.copy(in, copy, StandardCopyOption.REPLACE_EXISTING);
      read(copy.toFile(), MimeTypeUtils.IMAGE_PNG);
    } catch (IOException ex) {
      status.setText("Could not load the sample invoice");
    }
  }

  private void read(File file, MimeType type) {
    showPreview(file, type);

    new Thread(() -> {
      try {
        FileSystemResource image = new FileSystemResource(file);
        InvoiceData data = chat.prompt()
            .user(u -> u.text(PROMPT).media(type, image))
            .call()
            .entity(InvoiceData.class);
        if (data == null) {
          throw new IllegalStateException("the model returned no fields");
        }

        Environment.runLater(() -> show(data));
      } catch (Exception ex) {
        Environment.runLater(() -> {
          status.setText("Extraction failed: " + ex.getMessage());
        });
      } finally {
        // The image is only needed for the model call, so remove it once the model has read it.
        file.delete();
      }
    }).start();
  }

  // Shows the image the model reads, embedded as a data URL, so it needs no extra endpoint.
  private void showPreview(File file, MimeType type) {
    try {
      String data = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
      preview.setSrc("data:" + type + ";base64," + data).setVisible(true);
    } catch (IOException ex) {
      preview.setVisible(false);
    }
  }

  private void show(InvoiceData data) {
    status.setText("Extracted invoice " + orNotFound(data.invoiceNumber()));
    result.setText(orNotFound(data.vendor()) + ", " + orNotFound(data.invoiceDate()) + ", "
        + orNotFound(data.total()) + " " + orNotFound(data.currency()));
  }

  // The prompt asks for null when a field is unreadable, so say so instead of printing "null".
  private static String orNotFound(Object value) {
    return value != null ? value.toString() : "not found";
  }
}
