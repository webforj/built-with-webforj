package com.webforj.builtwithwebforj.tablesearch.search;

import java.util.concurrent.CompletableFuture;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class QueryParser {
  private final ChatClient chatClient;

  public QueryParser(ChatClient.Builder builder, ProductCatalog catalog) {
    this.chatClient = builder
        .defaultSystem("""
            Turn the user's search request into a product filter.
            Set only the fields the request mentions and leave the rest null.
            Category must be one of: %s.
            Prices are plain numbers without a currency symbol."""
            .formatted(String.join(", ", catalog.categories())))
        .build();
  }

  @Async
  public CompletableFuture<ProductQuery> parse(String text) {
    try {
      return CompletableFuture.completedFuture(
          chatClient.prompt().user(text).call().entity(ProductQuery.class));
    } catch (RuntimeException ex) {
      return CompletableFuture.failedFuture(ex);
    }
  }
}
