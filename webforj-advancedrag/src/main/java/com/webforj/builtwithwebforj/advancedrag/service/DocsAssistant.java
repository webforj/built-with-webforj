package com.webforj.builtwithwebforj.advancedrag.service;

import com.webforj.builtwithwebforj.advancedrag.rag.RagPipeline;
import com.webforj.builtwithwebforj.advancedrag.rag.Trace;
import java.util.concurrent.CompletableFuture;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DocsAssistant {

  /** The answer together with what each pipeline stage did to reach it. */
  public record Answer(String text, Trace trace, long millis) {}

  private final ChatClient chat;
  private final RagPipeline pipeline;

  public DocsAssistant(ChatClient.Builder builder, RagPipeline pipeline) {
    this.chat = builder.build();
    this.pipeline = pipeline;
  }

  @Async
  public CompletableFuture<Answer> ask(String question) {
    Trace trace = new Trace();
    long start = System.currentTimeMillis();
    String text = chat.prompt()
        .advisors(pipeline.advisor(trace))
        .user(question)
        .call()
        .content();
    return CompletableFuture.completedFuture(
        new Answer(text, trace, System.currentTimeMillis() - start));
  }
}
