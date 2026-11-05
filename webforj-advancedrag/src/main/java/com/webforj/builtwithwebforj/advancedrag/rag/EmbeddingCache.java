package com.webforj.builtwithwebforj.advancedrag.rag;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;

/**
 * Keeps the manual's embeddings in memory and in a file, so the manual is embedded once and a
 * restart costs no embedding calls. Questions are embedded on every call and never stored. The
 * file name carries the embedding model, so switching models never mixes two vector spaces.
 * Calls are paced for the Gemini free tier, which counts every text in a batch as one request.
 */
public class EmbeddingCache implements EmbeddingModel {
  private static final Logger log = LoggerFactory.getLogger(EmbeddingCache.class);
  private static final int BATCH = 40;
  private static final int ATTEMPTS = 5;

  private final EmbeddingModel delegate;
  private final Path file;
  private final Duration rateLimitWait;
  private final Map<String, float[]> cache;

  public EmbeddingCache(EmbeddingModel delegate, Path folder, String model, Duration rateLimitWait) {
    this.delegate = delegate;
    this.file = folder.resolve("embeddings-" + model + ".ser");
    this.rateLimitWait = rateLimitWait;
    this.cache = new ConcurrentHashMap<>(load(file));
  }

  /**
   * Embeds every text not cached yet, in small batches, and saves them to the file.
   */
  public void warm(List<String> texts) {
    List<String> missing = texts.stream().filter(text -> !cache.containsKey(text)).toList();
    for (int from = 0; from < missing.size(); from += BATCH) {
      List<String> batch = missing.subList(from, Math.min(from + BATCH, missing.size()));
      List<float[]> vectors = withRetry(() -> delegate.embed(batch));
      for (int i = 0; i < batch.size(); i++) {
        cache.put(batch.get(i), vectors.get(i));
      }
      save();
      log.info("Embedded {} of {} manual texts", from + batch.size(), missing.size());
    }
  }

  @Override
  public EmbeddingResponse call(EmbeddingRequest request) {
    List<Embedding> results = new ArrayList<>();
    List<String> texts = request.getInstructions();
    for (int i = 0; i < texts.size(); i++) {
      results.add(new Embedding(embed(texts.get(i)), i));
    }
    return new EmbeddingResponse(results);
  }

  @Override
  public float[] embed(Document document) {
    return embed(document.getText());
  }

  // No lock around the model call, so parallel questions don't wait on each other.
  @Override
  public float[] embed(String text) {
    float[] vector = cache.get(text);
    return vector != null ? vector : withRetry(() -> delegate.embed(text));
  }

  // Waits out the per-minute window when Gemini answers 429.
  private <T> T withRetry(Supplier<T> call) {
    for (int attempt = 1; ; attempt++) {
      try {
        return call.get();
      } catch (RuntimeException ex) {
        if (attempt == ATTEMPTS || !String.valueOf(ex.getMessage()).contains("429")) {
          throw ex;
        }
        log.info("Embedding rate limited, waiting {} seconds", rateLimitWait.toSeconds());
        try {
          Thread.sleep(rateLimitWait.toMillis());
        } catch (InterruptedException interrupted) {
          Thread.currentThread().interrupt();
          throw ex;
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static Map<String, float[]> load(Path file) {
    if (Files.exists(file)) {
      try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
        return (Map<String, float[]>) in.readObject();
      } catch (IOException | ClassNotFoundException ex) {
        log.warn("Ignoring unreadable embedding cache {}", file);
      }
    }
    return Map.of();
  }

  private synchronized void save() {
    try {
      Files.createDirectories(file.toAbsolutePath().getParent());
      try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
        out.writeObject(new HashMap<>(cache));
      }
    } catch (IOException ex) {
      log.warn("Could not save the embedding cache {}", file);
    }
  }
}
