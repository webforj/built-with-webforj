package com.webforj.builtwithwebforj.advancedrag.rag;

import java.util.ArrayList;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.postretrieval.document.DocumentPostProcessor;

public class LlmReranker implements DocumentPostProcessor {
  private final ChatClient chat;
  private final int keep;

  public LlmReranker(ChatClient.Builder builder, int keep) {
    this.chat = builder.build();
    this.keep = keep;
  }

  @Override
  public List<Document> process(Query query, List<Document> documents) {
    if (documents.size() <= keep) {
      return documents;
    }
    StringBuilder passages = new StringBuilder();
    for (int i = 0; i < documents.size(); i++) {
      passages.append("[").append(i).append("] ")
          .append(documents.get(i).getText()).append("\n\n");
    }

    String reply = chat.prompt()
        .system("""
            Rank the passages by how directly they answer the question.
            Reply with the passage numbers only, best first, separated by commas.
            """)
        .user("Question: " + query.text() + "\n\nPassages:\n" + passages)
        .call()
        .content();

    List<Document> ranked = new ArrayList<>();
    for (String token : String.valueOf(reply).split("[^0-9]+")) {
      // Skip empty tokens and numbers too long to be a passage index.
      if (token.isEmpty() || token.length() > 4) {
        continue;
      }
      int index = Integer.parseInt(token);
      if (index < documents.size() && !ranked.contains(documents.get(index))) {
        ranked.add(documents.get(index));
      }
    }
    // A reply without usable passage numbers keeps the retrieval order.
    if (ranked.isEmpty()) {
      return documents.subList(0, keep);
    }
    return ranked.size() > keep ? ranked.subList(0, keep) : ranked;
  }
}
