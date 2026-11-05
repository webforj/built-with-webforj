package com.webforj.builtwithwebforj.advancedrag.rag;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;

class EmbeddingCacheTest {
  @TempDir
  Path dir;

  @Test
  void shouldEmbedTheManualOnceAndReadItBackFromTheFile() {
    CountingModel model = new CountingModel(0);
    new EmbeddingCache(model, dir, "m1", Duration.ZERO).warm(List.of("a", "bb"));
    assertEquals(2, model.texts);
    assertTrue(Files.exists(dir.resolve("embeddings-m1.ser")));

    CountingModel second = new CountingModel(0);
    EmbeddingCache restarted = new EmbeddingCache(second, dir, "m1", Duration.ZERO);
    restarted.warm(List.of("a", "bb"));

    assertArrayEquals(new float[] {2}, restarted.embed(Document.builder().text("bb").build()));
    assertEquals(0, second.texts);
  }

  @Test
  void shouldKeepCachesOfDifferentModelsApart() {
    new EmbeddingCache(new CountingModel(0), dir, "m1", Duration.ZERO).warm(List.of("a"));
    CountingModel other = new CountingModel(0);

    new EmbeddingCache(other, dir, "m2", Duration.ZERO).warm(List.of("a"));

    assertEquals(1, other.texts);
  }

  @Test
  void shouldEmbedQuestionsWithoutStoringThem() {
    CountingModel model = new CountingModel(0);
    EmbeddingCache cache = new EmbeddingCache(model, dir, "m1", Duration.ZERO);

    cache.embed("question");
    EmbeddingResponse response = cache.call(new EmbeddingRequest(List.of("question"), null));

    assertEquals(2, model.texts);
    assertArrayEquals(new float[] {8}, response.getResult().getOutput());
  }

  @Test
  void shouldRetryAfterARateLimit() {
    CountingModel model = new CountingModel(2);

    new EmbeddingCache(model, dir, "m1", Duration.ZERO).embed("abc");

    assertEquals(1, model.texts);
  }

  @Test
  void shouldGiveUpOnOtherErrors() {
    EmbeddingModel broken = new CountingModel(0) {
      @Override
      public EmbeddingResponse call(EmbeddingRequest request) {
        throw new IllegalStateException("500 server error");
      }
    };

    assertThrows(IllegalStateException.class,
        () -> new EmbeddingCache(broken, dir, "m1", Duration.ZERO).embed("abc"));
  }

  // Embeds a text as its length and fails with a 429 for the first calls.
  private static class CountingModel implements EmbeddingModel {
    private int rateLimited;
    int texts;

    CountingModel(int rateLimited) {
      this.rateLimited = rateLimited;
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
      if (rateLimited > 0) {
        rateLimited--;
        throw new IllegalStateException("429 too many requests");
      }
      List<String> inputs = request.getInstructions();
      texts += inputs.size();
      return new EmbeddingResponse(inputs.stream()
          .map(text -> new Embedding(new float[] {text.length()}, 0)).toList());
    }

    @Override
    public float[] embed(Document document) {
      return embed(document.getText());
    }
  }
}
