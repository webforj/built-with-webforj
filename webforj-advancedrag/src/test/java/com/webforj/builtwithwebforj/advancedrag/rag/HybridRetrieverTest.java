package com.webforj.builtwithwebforj.advancedrag.rag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

class HybridRetrieverTest {

  @Test
  void shouldRankADocumentBothSearchesFoundFirst() {
    Document a = doc("a");
    Document b = doc("b");
    Document c = doc("c");
    List<Document> fused = retrieve(List.of(a, b), List.of(c, b));

    assertEquals(List.of("b", "a", "c"), fused.stream().map(Document::getId).toList());
  }

  @Test
  void shouldCarryTheFusedScoreSoTheJoinerKeepsTheOrder() {
    List<Document> fused = retrieve(List.of(doc("a").mutate().score(0.99).build()),
        List.of(doc("b"), doc("a")));

    assertEquals("a", fused.get(0).getId());
    assertEquals(1.0 / 61 + 1.0 / 62, fused.get(0).getScore(), 1e-9);
    assertTrue(fused.get(0).getScore() > fused.get(1).getScore());
  }

  @Test
  void shouldKeepOnlyTheTopTen() {
    List<Document> many = IntStream.range(0, 15).mapToObj(i -> doc("d" + i)).toList();

    assertEquals(10, retrieve(many, List.of()).size());
  }

  private static List<Document> retrieve(List<Document> vector, List<Document> keyword) {
    VectorStore vectors = mock(VectorStore.class);
    when(vectors.similaritySearch(any(SearchRequest.class))).thenReturn(vector);
    return new HybridRetriever(vectors, (text, limit) -> keyword).retrieve(new Query("q"));
  }

  private static Document doc(String id) {
    return Document.builder().id(id).text(id).build();
  }
}
