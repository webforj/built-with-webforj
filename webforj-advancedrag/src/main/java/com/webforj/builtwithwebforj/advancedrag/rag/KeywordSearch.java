package com.webforj.builtwithwebforj.advancedrag.rag;

import java.util.List;
import org.springframework.ai.document.Document;

public interface KeywordSearch {
  List<Document> search(String text, int limit);
}
