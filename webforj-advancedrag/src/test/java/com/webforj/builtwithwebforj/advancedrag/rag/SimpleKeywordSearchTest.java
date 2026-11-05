package com.webforj.builtwithwebforj.advancedrag.rag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

class SimpleKeywordSearchTest {
  private final SimpleKeywordSearch search = new SimpleKeywordSearch(List.of(
      doc("cancel", "Cancel a subscription in Billing."),
      doc("e4012", "Error E-4012 means the license token has expired."),
      doc("e1003", "Error E-1003 is a network timeout.")));

  @Test
  void shouldRankTheEntryWithMostMatchingWordsFirst() {
    assertEquals(List.of("e4012", "e1003"), ids(search.search("What does error E-4012 mean?", 5)));
  }

  @Test
  void shouldRespectTheLimitAndSkipShortWords() {
    assertEquals(List.of("e4012"), ids(search.search("error E-4012", 1)));
    assertEquals(List.of(), ids(search.search("is a the", 5)));
  }

  private static Document doc(String id, String text) {
    return Document.builder().id(id).text(text).build();
  }

  private static List<String> ids(List<Document> docs) {
    return docs.stream().map(Document::getId).toList();
  }
}
