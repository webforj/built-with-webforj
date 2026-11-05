package com.webforj.builtwithwebforj.advancedrag.rag;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;

/**
 * Builds a trace the way a real run fills it, for tests outside this package.
 */
public final class TraceFixtures {
  public static final Document HIT =
      Document.builder().id("E-4012").text("Error E-4012 means the token has expired.").build();
  public static final Document OTHER =
      Document.builder().id("E-4212").text("Error E-4212: the file upload timed out.").build();

  private TraceFixtures() {}

  public static Trace sample() {
    Trace trace = new Trace();
    trace.transformer(query -> new Query("error E-4012")).transform(new Query("E-4012?"));
    trace.expander(query -> List.of(query)).expand(new Query("error E-4012"));
    trace.keywordSearch((text, limit) -> List.of(HIT, OTHER)).search("error E-4012", 20);
    trace.retriever(query -> List.of(HIT, OTHER)).retrieve(new Query("error E-4012"));
    trace.postProcessor((query, docs) -> List.of(HIT))
        .process(new Query("error E-4012"), List.of(HIT, OTHER));
    return trace;
  }
}
