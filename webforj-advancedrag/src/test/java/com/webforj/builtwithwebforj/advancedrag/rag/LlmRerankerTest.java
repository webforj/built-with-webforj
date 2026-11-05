package com.webforj.builtwithwebforj.advancedrag.rag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;

class LlmRerankerTest {
  private final List<Document> docs =
      IntStream.range(0, 6).mapToObj(i -> Document.builder().id("d" + i).text("t" + i).build())
          .toList();

  @Test
  void shouldKeepThePassagesInTheModelsOrder() {
    assertEquals(List.of("d4", "d1"), ids(rerank("4, 1", 2)));
  }

  @Test
  void shouldIgnoreRepeatedAndUnknownNumbers() {
    assertEquals(List.of("d2", "d0"), ids(rerank("Best: 2, 2, 99, 12345678901, 0", 3)));
  }

  @Test
  void shouldFallBackToTheRetrievalOrderWhenTheReplyHasNoPassages() {
    assertEquals(List.of("d0", "d1"), ids(rerank("None of these passages help.", 2)));
  }

  @Test
  void shouldFallBackWhenTheModelSendsNothing() {
    assertEquals(List.of("d0", "d1"), ids(rerank(null, 2)));
  }

  @Test
  void shouldNotCallTheModelForAShortList() {
    LlmReranker reranker = new LlmReranker(mock(ChatClient.Builder.class), 10);

    assertEquals(docs, reranker.process(new Query("q"), docs));
  }

  private List<Document> rerank(String reply, int keep) {
    ChatClient chat = mock(ChatClient.class, Answers.RETURNS_DEEP_STUBS);
    when(chat.prompt().system(anyString()).user(anyString()).call().content()).thenReturn(reply);
    ChatClient.Builder builder = mock(ChatClient.Builder.class);
    when(builder.build()).thenReturn(chat);
    return new LlmReranker(builder, keep).process(new Query("q"), docs);
  }

  private static List<String> ids(List<Document> docs) {
    return docs.stream().map(Document::getId).toList();
  }
}
