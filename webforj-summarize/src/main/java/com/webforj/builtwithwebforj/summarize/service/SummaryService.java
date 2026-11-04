package com.webforj.builtwithwebforj.summarize.service;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {
  private static final int MAX_CHARS = 12_000;
  private final ChatClient chatClient;

  public SummaryService(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultSystem("You summarize documents in at most five bullet points.")
        .build();
  }

  @Async
  public CompletableFuture<String> summarize(Path file) {
    try {
      String text = extract(file);
      String input = text.length() > MAX_CHARS ? text.substring(0, MAX_CHARS) : text;
      String summary = chatClient.prompt()
          .user("Summarize this document:\n\n" + input)
          .call()
          .content();
      return CompletableFuture.completedFuture(summary);
    } catch (RuntimeException ex) {
      return CompletableFuture.failedFuture(ex);
    } finally {
      file.toFile().delete();
    }
  }

  private String extract(Path file) {
    List<Document> parts = new TikaDocumentReader(new FileSystemResource(file)).get();
    return parts.stream().map(Document::getText).collect(Collectors.joining("\n"));
  }
}
