package com.webforj.builtwithwebforj.smartform.views;

import static com.webforj.testbed.assertion.WebforjAssertions.verifyThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webforj.automation.act.UserInteraction;
import com.webforj.automation.act.component.upload.UploadInteraction;
import com.webforj.automation.find.ComponentLocator;
import com.webforj.builtwithwebforj.smartform.model.SupportTicket;
import com.webforj.builtwithwebforj.smartform.service.TicketExtractor;
import com.webforj.component.button.Button;
import com.webforj.component.field.TextArea;
import com.webforj.component.field.TextField;
import com.webforj.component.toast.Toast;
import com.webforj.component.upload.Upload;
import com.webforj.testbed.WebforjTestbed;
import com.webforj.testbed.annotation.WebforjTest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@WebforjTest(routeClasses = TicketView.class)
class TicketViewTest {

  @MockitoBean
  private TicketExtractor extractor;

  @Test
  void shouldPasteTheSampleEmail(WebforjTestbed context) {
    context.navigate(TicketView.class);

    UserInteraction.click(button("Use a sample email"));

    assertTrue(pasted().getValue().contains("maria.keller@nordwind.example"));
  }

  @Test
  void shouldFillTheFormFromTheText(WebforjTestbed context) {
    when(extractor.extract("Login broken")).thenReturn(
        CompletableFuture.completedFuture(ticket("Maria Keller", "maria@nordwind.example")));
    context.navigate(TicketView.class);

    UserInteraction.type(pasted(), "Login broken");
    UserInteraction.click(button("Fill form"));

    verifyThat(field("Name")).hasValue("Maria Keller");
    verifyThat(field("Email")).hasValue("maria@nordwind.example");
    verifyThat(button("Fill form")).isEnabled();
  }

  @Test
  void shouldSaveAValidTicket(WebforjTestbed context) {
    context.navigate(TicketView.class);

    UserInteraction.type(field("Name"), "Maria Keller");
    UserInteraction.type(field("Email"), "maria@nordwind.example");
    UserInteraction.type(field("Priority"), "HIGH");
    UserInteraction.click(button("Save ticket"));

    assertEquals("Ticket from Maria Keller saved",
        ComponentLocator.of(Toast.class).locate().getText());
  }

  @Test
  void shouldClearAFailedFillOnTheNextSuccess(WebforjTestbed context) {
    when(extractor.extract("Login broken")).thenReturn(
        CompletableFuture.failedFuture(new IllegalStateException("model down")),
        CompletableFuture.completedFuture(ticket("Maria Keller", "maria@nordwind.example")));
    context.navigate(TicketView.class);
    UserInteraction.type(pasted(), "Login broken");

    UserInteraction.click(button("Fill form"));
    assertTrue(pasted().isInvalid());

    UserInteraction.click(button("Fill form"));
    assertFalse(pasted().isInvalid());
    verifyThat(field("Name")).hasValue("Maria Keller");
  }

  @Test
  void shouldTreatAnEmptyModelReplyAsAFailure(WebforjTestbed context) {
    when(extractor.extract("Login broken"))
        .thenReturn(CompletableFuture.completedFuture(null));
    context.navigate(TicketView.class);

    UserInteraction.type(pasted(), "Login broken");
    UserInteraction.click(button("Fill form"));

    assertTrue(pasted().isInvalid());
    assertEquals("Could not read that text. Fill the form by hand.",
        pasted().getInvalidMessage());
    verifyThat(button("Fill form")).isEnabled();
  }

  @Test
  void shouldNotCallTheModelWithoutText(WebforjTestbed context) {
    context.navigate(TicketView.class);

    UserInteraction.click(button("Fill form"));

    verify(extractor, never()).extract(anyString());
    assertTrue(pasted().isInvalid());
    assertEquals("Paste or upload some text first.", pasted().getInvalidMessage());
    verifyThat(field("Name")).hasValue("");
  }

  @Test
  void shouldDeleteAnUploadedFileOnceItIsRead(WebforjTestbed context) throws IOException {
    String name = UUID.randomUUID() + ".txt";
    context.navigate(TicketView.class);
    context.pickFile(name, "Login broken".getBytes(StandardCharsets.UTF_8));

    UserInteraction.perform(
        ComponentLocator.of(Upload.class).locate(), UploadInteraction.upload(name));

    assertEquals("Login broken", pasted().getValue());
    assertFalse(uploadedFileExists(name), "the uploaded file should be deleted once it is read");
  }

  // Looks for the file in the upload folders the view creates under the system temp folder.
  private static boolean uploadedFileExists(String name) throws IOException {
    try (Stream<Path> dirs = Files.list(Path.of(System.getProperty("java.io.tmpdir")))) {
      for (Path dir : dirs.filter(dir -> dir.getFileName().toString().startsWith("smartform")
          && Files.isDirectory(dir)).toList()) {
        try (Stream<Path> files = Files.list(dir)) {
          if (files.anyMatch(file -> file.getFileName().toString().endsWith(name))) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static SupportTicket ticket(String name, String email) {
    SupportTicket ticket = new SupportTicket();
    ticket.setName(name);
    ticket.setEmail(email);
    return ticket;
  }

  private static TextArea pasted() {
    return ComponentLocator.of(TextArea.class).byLabel("Paste an email or note").locate();
  }

  private static TextField field(String label) {
    return ComponentLocator.of(TextField.class).byLabel(label).locate();
  }

  private static Button button(String text) {
    return ComponentLocator.of(Button.class).byText(text).locate();
  }
}
