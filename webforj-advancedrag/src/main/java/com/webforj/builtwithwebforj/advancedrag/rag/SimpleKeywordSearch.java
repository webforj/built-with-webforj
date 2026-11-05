package com.webforj.builtwithwebforj.advancedrag.rag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.ai.document.Document;

/**
 * Ranks documents by how many words of the question each one contains. A real app would use
 * PostgreSQL full-text search or a Lucene index here.
 */
public class SimpleKeywordSearch implements KeywordSearch {
  private final List<Document> docs;

  public SimpleKeywordSearch(List<Document> docs) {
    this.docs = docs;
  }

  @Override
  public List<Document> search(String text, int limit) {
    List<String> terms = List.of(text.toLowerCase(Locale.ROOT).split("[^a-z0-9-]+")).stream()
        .filter(term -> term.length() > 3)
        .toList();
    List<Document> hits = new ArrayList<>();
    for (Document doc : docs) {
      if (matches(doc, terms) > 0) {
        hits.add(doc);
      }
    }
    hits.sort(Comparator.comparingLong((Document doc) -> matches(doc, terms)).reversed());
    return hits.size() > limit ? hits.subList(0, limit) : hits;
  }

  private static long matches(Document doc, List<String> terms) {
    String body = doc.getText().toLowerCase(Locale.ROOT);
    return terms.stream().filter(body::contains).count();
  }
}
