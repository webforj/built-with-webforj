package com.webforj.builtwithwebforj.askdocs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.core.io.FileSystemResource;
import reactor.core.publisher.Flux;

class KnowledgeBaseTest {
  @TempDir
  Path dir;

  private KnowledgeBase kb;

  @BeforeEach
  void setUp() {
    kb = new KnowledgeBase(new KeywordEmbeddings(), ChatClient.builder(new FixedChat()));
  }

  @Test
  void shouldStoreAShortDocumentAsOneChunk() throws Exception {
    assertEquals(1, kb.add(file("hotels.txt", "The hotel limit is 150 EUR."), "hotels.txt"));
  }

  @Test
  void shouldCiteOnlyTheDocumentThatMatchesTheQuestion() throws Exception {
    kb.add(file("hotels.txt", "The hotel limit is 150 EUR."), "hotels.txt");
    kb.add(file("meals.txt", "Meals are covered up to 40 EUR."), "meals.txt");

    ChatClientResponse response = kb.ask("What is the hotel limit?").blockFirst();

    assertEquals(List.of("hotels.txt"), KnowledgeBase.sourcesOf(response));
  }

  private FileSystemResource file(String name, String text) throws Exception {
    return new FileSystemResource(Files.writeString(dir.resolve(name), text));
  }

  // Embeds a text by whether it mentions hotels or meals, so retrieval needs no API key.
  private static final class KeywordEmbeddings implements EmbeddingModel {
    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
      return new EmbeddingResponse(request.getInstructions().stream()
          .map(text -> new Embedding(vector(text), 0)).toList());
    }

    @Override
    public float[] embed(Document document) {
      return vector(document.getText());
    }

    private static float[] vector(String text) {
      String lower = text.toLowerCase();
      return new float[] {lower.contains("hotel") ? 1 : 0, lower.contains("meal") ? 1 : 0, 0.01f};
    }
  }

  private static final class FixedChat implements ChatModel {
    @Override
    public ChatResponse call(Prompt prompt) {
      return new ChatResponse(List.of(new Generation(new AssistantMessage("150 EUR."))));
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
      return Flux.just(call(prompt));
    }
  }
}
