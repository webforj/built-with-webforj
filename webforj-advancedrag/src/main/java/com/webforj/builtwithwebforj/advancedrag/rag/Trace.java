package com.webforj.builtwithwebforj.advancedrag.rag;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;
import org.springframework.ai.rag.preretrieval.query.expansion.QueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;

/**
 * Records what every stage of one question's pipeline received and returned, and how long it
 * took. Each wrapper hands the call to the real stage unchanged, so the trace shows the real run.
 * Retrieval runs once per expanded query, possibly in parallel, so the fields are thread safe.
 */
public final class Trace {

  /** What the two searches and their fusion returned for one query. */
  public static final class Search {
    private volatile List<Document> meaning = List.of();
    private volatile List<Document> keyword = List.of();
    private volatile List<Document> merged = List.of();

    public List<Document> meaning() {
      return meaning;
    }

    public List<Document> keyword() {
      return keyword;
    }

    public List<Document> merged() {
      return merged;
    }
  }

  private volatile String rewritten;
  private volatile List<String> expanded = List.of();
  private final Map<String, Search> searches = new ConcurrentHashMap<>();
  private volatile List<Document> candidates = List.of();
  private volatile List<Document> kept = List.of();
  private volatile long rewriteMillis;
  private volatile long expandMillis;
  private volatile long rerankMillis;
  private final AtomicLong searchStart = new AtomicLong(Long.MAX_VALUE);
  private final AtomicLong searchEnd = new AtomicLong();

  public String rewritten() {
    return rewritten;
  }

  public List<String> expanded() {
    return expanded;
  }

  public Search search(String query) {
    return searches.computeIfAbsent(query, key -> new Search());
  }

  public List<Document> candidates() {
    return candidates;
  }

  public List<Document> kept() {
    return kept;
  }

  public long rewriteMillis() {
    return rewriteMillis;
  }

  public long expandMillis() {
    return expandMillis;
  }

  public long searchMillis() {
    return searchEnd.get() == 0 ? 0 : searchEnd.get() - searchStart.get();
  }

  public long rerankMillis() {
    return rerankMillis;
  }

  QueryTransformer transformer(QueryTransformer stage) {
    return query -> {
      long start = System.currentTimeMillis();
      Query result = stage.transform(query);
      rewriteMillis = System.currentTimeMillis() - start;
      rewritten = result.text();
      return result;
    };
  }

  QueryExpander expander(QueryExpander stage) {
    return query -> {
      long start = System.currentTimeMillis();
      List<Query> result = stage.expand(query);
      expandMillis = System.currentTimeMillis() - start;
      expanded = result.stream().map(Query::text).toList();
      return result;
    };
  }

  DocumentRetriever retriever(DocumentRetriever stage) {
    return query -> {
      searchStart.accumulateAndGet(System.currentTimeMillis(), Math::min);
      List<Document> result = stage.retrieve(query);
      search(query.text()).merged = result;
      searchEnd.accumulateAndGet(System.currentTimeMillis(), Math::max);
      return result;
    };
  }

  DocumentPostProcessor postProcessor(DocumentPostProcessor stage) {
    return (query, documents) -> {
      candidates = documents;
      long start = System.currentTimeMillis();
      List<Document> result = stage.process(query, documents);
      rerankMillis = System.currentTimeMillis() - start;
      kept = result;
      return result;
    };
  }

  KeywordSearch keywordSearch(KeywordSearch stage) {
    return (text, limit) -> {
      List<Document> result = stage.search(text, limit);
      search(text).keyword = result;
      return result;
    };
  }

  VectorStore vectorStore(VectorStore stage) {
    return new VectorStore() {
      @Override
      public List<Document> similaritySearch(SearchRequest request) {
        List<Document> result = stage.similaritySearch(request);
        search(request.getQuery()).meaning = result;
        return result;
      }

      @Override
      public void add(List<Document> documents) {
        stage.add(documents);
      }

      @Override
      public void delete(List<String> ids) {
        stage.delete(ids);
      }

      @Override
      public void delete(Filter.Expression expression) {
        stage.delete(expression);
      }
    };
  }
}
