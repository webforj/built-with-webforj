package com.webforj.builtwithwebforj.advancedrag.rag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

public class HybridRetriever implements DocumentRetriever {
  private static final int RRF_K = 60;
  private static final int CANDIDATES = 20;
  private static final int TOP = 10;

  private final VectorStore vectorStore;
  private final KeywordSearch keywordSearch;

  public HybridRetriever(VectorStore vectorStore, KeywordSearch keywordSearch) {
    this.vectorStore = vectorStore;
    this.keywordSearch = keywordSearch;
  }

  @Override
  public List<Document> retrieve(Query query) {
    List<Document> semantic = vectorStore.similaritySearch(
        SearchRequest.builder().query(query.text()).topK(CANDIDATES).build());
    List<Document> keyword = keywordSearch.search(query.text(), CANDIDATES);

    Map<String, Double> scores = new LinkedHashMap<>();
    Map<String, Document> byId = new LinkedHashMap<>();
    for (List<Document> ranking : List.of(semantic, keyword)) {
      for (int rank = 0; rank < ranking.size(); rank++) {
        Document doc = ranking.get(rank);
        byId.putIfAbsent(doc.getId(), doc);
        scores.merge(doc.getId(), 1.0 / (RRF_K + rank + 1), Double::sum);
      }
    }

    // Spring AI's default joiner sorts by Document.getScore(), so the fused score has to replace
    // the vector score, otherwise the joiner undoes the fusion.
    List<Document> fused = new ArrayList<>();
    for (Document doc : byId.values()) {
      fused.add(doc.mutate().score(scores.get(doc.getId())).build());
    }
    fused.sort(Comparator.comparingDouble(Document::getScore).reversed());
    return fused.size() > TOP ? fused.subList(0, TOP) : fused;
  }
}
