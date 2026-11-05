package com.webforj.builtwithwebforj.invoicereader.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.act.component.upload.UploadInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.invoicereader.views.InvoiceUploadView.InvoiceData;
import com.webforj.component.button.Button;
import com.webforj.component.html.elements.Img;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.component.upload.Upload;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.math.BigDecimal;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = InvoiceUploadView.class)
class InvoiceUploadViewTest {

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  private ChatClient chat;

  @Test
  void shouldShowTheExtractedFields(WebforjTestbed context) throws InterruptedException {
    when(chat.prompt().user(any(Consumer.class)).call()
        .entity(InvoiceData.class))
        .thenReturn(new InvoiceData("webforJ", "INV-42", "2026-07-01", "EUR",
            new BigDecimal("118.00")));
    context.navigate(InvoiceUploadView.class);
    context.pickFile("invoice.png", new byte[] {1, 2, 3});

    UserInteraction.perform(
        ComponentLocator.of(Upload.class).locate(), UploadInteraction.upload("invoice.png"));

    waitFor(() -> paragraph("Extracted invoice INV-42") != null);
    assertEquals("webforJ, 2026-07-01, 118.00 EUR",
        paragraph("webforJ, 2026-07-01, 118.00 EUR").getText());
  }

  @Test
  void shouldReadTheSampleInvoice(WebforjTestbed context) throws InterruptedException {
    when(chat.prompt().user(any(Consumer.class)).call().entity(InvoiceData.class))
        .thenReturn(new InvoiceData("webforJ", "INV-2026-0142", "2026-07-14", "EUR",
            new BigDecimal("743.16")));
    context.navigate(InvoiceUploadView.class);

    UserInteraction.click(ComponentLocator.of(Button.class).byText("Try a sample invoice").locate());

    assertTrue(ComponentLocator.of(Img.class).locate().getSrc().startsWith("data:image/png;base64,"));
    waitFor(() -> paragraph("Extracted invoice INV-2026-0142") != null);
    assertEquals("webforJ, 2026-07-14, 743.16 EUR",
        paragraph("webforJ, 2026-07-14, 743.16 EUR").getText());
  }

  @Test
  void shouldMarkFieldsTheModelCouldNotRead(WebforjTestbed context) throws InterruptedException {
    when(chat.prompt().user(any(Consumer.class)).call().entity(InvoiceData.class))
        .thenReturn(new InvoiceData("webforJ", null, null, "EUR", null));
    context.navigate(InvoiceUploadView.class);

    UserInteraction.click(ComponentLocator.of(Button.class).byText("Try a sample invoice").locate());

    waitFor(() -> paragraph("Extracted invoice not found") != null);
    assertEquals("webforJ, not found, not found EUR",
        paragraph("webforJ, not found, not found EUR").getText());
  }

  @Test
  void shouldReportAnEmptyModelReply(WebforjTestbed context) throws InterruptedException {
    when(chat.prompt().user(any(Consumer.class)).call().entity(InvoiceData.class))
        .thenReturn(null);
    context.navigate(InvoiceUploadView.class);

    UserInteraction.click(ComponentLocator.of(Button.class).byText("Try a sample invoice").locate());

    String failed = "Extraction failed: the model returned no fields";
    waitFor(() -> paragraph(failed) != null);
    assertEquals(failed, paragraph(failed).getText());
  }

  private static Paragraph paragraph(String text) {
    var locator = ComponentLocator.of(Paragraph.class).byText(text);
    return locator.isPresent() ? locator.locate() : null;
  }

  // The view reads the image on its own thread, so give it a moment to report back.
  private static void waitFor(BooleanSupplier done) throws InterruptedException {
    for (int i = 0; i < 50 && !done.getAsBoolean(); i++) {
      Thread.sleep(100);
    }
  }
}
