package com.webforj.builtwithwebforj.advancedrag.rag;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Measures whether hybrid search ranks exact error codes better than vector search alone, on the
 * 200 generated manual entries whose codes look alike.
 *
 * <p>Not part of the normal build: Maven only runs classes ending in Test, and this one also
 * needs a real Gemini embedding key. Run it by hand, see the README. Embeddings are cached in
 * {@code .cache/}, so only the first run calls Gemini for the manual.
 */
@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "SPRING_AI_GOOGLE_GENAI_EMBEDDING_API_KEY", matches = ".+")
class RetrievalEval {
  private static final int TOP = 5;
  private static final int QUESTIONS = 20;

  @Autowired
  private EmbeddingModel embeddings;

  @Value("${spring.ai.google.genai.embedding.text.options.model}")
  private String model;

  @Test
  void compareVectorAndHybridSearch() throws InterruptedException {
    List<Document> manual = Manual.generated();
    EmbeddingCache cached = new EmbeddingCache(embeddings, Path.of(".cache"), model,
        Duration.ofSeconds(61));
    cached.warm(manual.stream().map(Document::getText).toList());
    SimpleVectorStore vectors = SimpleVectorStore.builder(cached).build();
    vectors.add(manual);
    HybridRetriever hybrid = new HybridRetriever(vectors, new SimpleKeywordSearch(manual));

    Random random = new Random(7);
    int[] vectorRanks = new int[QUESTIONS];
    int[] hybridRanks = new int[QUESTIONS];
    for (int i = 0; i < QUESTIONS; i++) {
      Document expected = manual.get(random.nextInt(manual.size()));
      String question = "What should I do about error " + expected.getId() + "?";

      vectorRanks[i] = rank(vectors.similaritySearch(
          SearchRequest.builder().query(question).topK(20).build()), expected);
      hybridRanks[i] = rank(hybrid.retrieve(new Query(question)), expected);
      System.out.printf("EVAL %-48s vector=#%s hybrid=#%s%n", question, show(vectorRanks[i]),
          show(hybridRanks[i]));
    }

    System.out.printf("EVAL Exact error codes, %d questions:%n", QUESTIONS);
    report("vector only", vectorRanks);
    report("hybrid     ", hybridRanks);
  }

  private static void report(String name, int[] ranks) {
    long first = Arrays.stream(ranks).filter(rank -> rank == 1).count();
    long top = Arrays.stream(ranks).filter(rank -> rank >= 1 && rank <= TOP).count();
    System.out.printf("EVAL   %s : ranked first %d/%d, in top %d %d/%d%n", name, first,
        ranks.length, TOP, top, ranks.length);
  }

  // 1-based position of the expected document, or 0 when it is missing.
  private static int rank(List<Document> docs, Document expected) {
    for (int i = 0; i < docs.size(); i++) {
      if (docs.get(i).getId().equals(expected.getId())) {
        return i + 1;
      }
    }
    return 0;
  }

  private static String show(int rank) {
    return rank == 0 ? "missing" : String.valueOf(rank);
  }
}
