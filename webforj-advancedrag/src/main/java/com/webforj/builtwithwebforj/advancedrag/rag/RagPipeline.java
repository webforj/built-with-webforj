package com.webforj.builtwithwebforj.advancedrag.rag;

import java.nio.file.Path;
import java.time.Duration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * The retrieval pipeline from the blog post. The stages and their settings are the same, but the
 * advisor is built per question instead of once as a bean, so each run gets its own {@link Trace}.
 */
@Configuration
public class RagPipeline implements DisposableBean {
  private final ChatClient.Builder builder;
  private final VectorStore vectorStore;
  private final KeywordSearch keywordSearch;
  // An advisor without an executor creates its own thread pool, so every advisor shares this one.
  private final ThreadPoolTaskExecutor retrievalThreads = new ThreadPoolTaskExecutor();

  public RagPipeline(ChatClient.Builder builder, VectorStore vectorStore,
      KeywordSearch keywordSearch) {
    this.builder = builder;
    this.vectorStore = vectorStore;
    this.keywordSearch = keywordSearch;
    retrievalThreads.setThreadNamePrefix("rag-retrieval-");
    retrievalThreads.setCorePoolSize(4);
    retrievalThreads.setMaxPoolSize(16);
    retrievalThreads.initialize();
  }

  public RetrievalAugmentationAdvisor advisor(Trace trace) {
    return RetrievalAugmentationAdvisor.builder()
        .queryTransformers(trace.transformer(RewriteQueryTransformer.builder()
            .chatClientBuilder(builder.clone())
            .build()))
        .queryExpander(trace.expander(MultiQueryExpander.builder()
            .chatClientBuilder(builder.clone())
            .numberOfQueries(3)
            .includeOriginal(true)
            .build()))
        .documentRetriever(trace.retriever(new HybridRetriever(
            trace.vectorStore(vectorStore), trace.keywordSearch(keywordSearch))))
        .documentPostProcessors(trace.postProcessor(new LlmReranker(builder.clone(), 5)))
        .taskExecutor(retrievalThreads)
        .build();
  }

  @Override
  public void destroy() {
    retrievalThreads.shutdown();
  }

  @Configuration
  static class Store {
    // Not a bean, so the Gemini embedding model stays the only EmbeddingModel in the context.
    private final EmbeddingCache embeddings;

    Store(EmbeddingModel embeddings,
        @Value("${spring.ai.google.genai.embedding.text.options.model}") String model) {
      this.embeddings = new EmbeddingCache(embeddings, Path.of(".cache"), model,
          Duration.ofSeconds(61));
    }

    @Bean
    VectorStore vectorStore() {
      return SimpleVectorStore.builder(embeddings).build();
    }

    @Bean
    KeywordSearch keywordSearch() {
      return new SimpleKeywordSearch(Manual.all());
    }

    // Runs while the context starts, before the web server accepts requests, so no question
    // can reach an empty store. The first start embeds the manual, later starts read the
    // cache file. Tests skip it because embedding calls the model.
    @Bean
    @Profile("!test")
    InitializingBean loadManual(VectorStore vectorStore) {
      return () -> {
        embeddings.warm(Manual.all().stream().map(Document::getText).toList());
        vectorStore.add(Manual.all());
      };
    }
  }
}
