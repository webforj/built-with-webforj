package com.webforj.builtwithwebforj.advancedrag.rag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webforj.builtwithwebforj.advancedrag.service.DocsAssistant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;

class PipelineWiringTest {

  @Test
  void shouldBuildAnAdvisorPerQuestionAndAnswerWithItsTrace() throws Exception {
    ChatClient.Builder builder = mock(ChatClient.Builder.class, Answers.RETURNS_DEEP_STUBS);
    when(builder.clone()).thenReturn(builder);
    RagPipeline pipeline = new RagPipeline(builder, mock(VectorStore.class),
        mock(KeywordSearch.class));

    assertNotNull(pipeline.advisor(new Trace()));
    DocsAssistant.Answer answer = new DocsAssistant(builder, pipeline).ask("q").get();

    assertNotNull(answer.trace());
    pipeline.destroy();
  }

  @Test
  void shouldRecordVectorResultsAndPassWritesThrough() {
    Trace trace = new Trace();
    VectorStore store = mock(VectorStore.class);
    VectorStore recorded = trace.vectorStore(store);
    Document doc = Document.builder().id("a").text("a").build();
    Filter.Expression expression = mock(Filter.Expression.class);

    recorded.similaritySearch(SearchRequest.builder().query("q").build());
    recorded.add(List.of(doc));
    recorded.delete(List.of("a"));
    recorded.delete(expression);

    assertEquals(List.of(), trace.search("q").meaning());
    verify(store).add(List.of(doc));
    verify(store).delete(List.of("a"));
    verify(store).delete(expression);
  }
}
