package com.webforj.builtwithwebforj.smartform.service;

import com.webforj.builtwithwebforj.smartform.model.SupportTicket;
import java.util.concurrent.CompletableFuture;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Turns free text into a {@link SupportTicket} with Spring AI structured output.
 */
@Service
public class TicketExtractor {
  private final ChatClient chatClient;

  public TicketExtractor(ChatClient.Builder builder) {
    this.chatClient = builder
        .defaultSystem("""
            Extract support ticket fields from the user's text.
            Leave a field empty if the text does not state it.
            Priority is LOW, NORMAL or HIGH.""")
        .build();
  }

  /**
   * Runs on a Spring task thread because the model call blocks for a few seconds.
   */
  @Async
  public CompletableFuture<SupportTicket> extract(String text) {
    return CompletableFuture.completedFuture(
        chatClient.prompt().user(text).call().entity(SupportTicket.class));
  }
}
