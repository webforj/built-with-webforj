package com.webforj.builtwithwebforj.advancedrag.rag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;

class TraceTest {

  @Test
  void shouldRecordWhatEachStageReturned() {
    Trace trace = new Trace();
    Document hit = Document.builder().id("E-4012").text("token expired").build();

    trace.transformer(query -> new Query("error E-4012")).transform(new Query("E-4012?"));
    trace.expander(query -> List.of(query, new Query("E-4012 fix"))).expand(new Query("x"));
    trace.keywordSearch((text, limit) -> List.of(hit)).search("error E-4012", 20);
    trace.retriever(query -> List.of(hit)).retrieve(new Query("error E-4012"));
    trace.postProcessor((query, docs) -> docs.subList(0, 1))
        .process(new Query("error E-4012"), List.of(hit, hit));

    assertEquals("error E-4012", trace.rewritten());
    assertEquals(List.of("x", "E-4012 fix"), trace.expanded());
    assertEquals(List.of(hit), trace.search("error E-4012").keyword());
    assertEquals(List.of(hit), trace.search("error E-4012").merged());
    assertEquals(2, trace.candidates().size());
    assertEquals(List.of(hit), trace.kept());
  }
}
