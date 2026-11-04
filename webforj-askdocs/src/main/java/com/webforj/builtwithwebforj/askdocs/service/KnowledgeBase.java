package com.webforj.builtwithwebforj.askdocs.service;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * The whole RAG pipeline in one Spring service.
 *
 * <p>
 * Indexing: read a file, split it into chunks, embed the chunks into a vector store. Asking: find
 * the chunks closest to the question and let the model answer from them only.
 * </p>
 */
@Service
public class KnowledgeBase {
  private final VectorStore store;
  private final ChatClient chat;

  public KnowledgeBase(EmbeddingModel embeddings, ChatClient.Builder builder) {
    // In-memory vector store. Swap it for a database-backed VectorStore to keep documents.
    store = SimpleVectorStore.builder(embeddings).build();

    chat = builder
        .defaultSystem("Answer in short Markdown. Use only the context you are given.")
        .defaultAdvisors(RetrievalAugmentationAdvisor.builder()
            .documentRetriever(VectorStoreDocumentRetriever.builder()
                .vectorStore(store)
                .similarityThreshold(0.5)
                .topK(4)
                .build())
            .build())
        .build();
  }

  /**
   * Reads, splits and embeds one document.
   *
   * @param document an uploaded file or a bundled resource
   * @param name the name to show as the source of an answer
   * @return the number of chunks stored
   */
  public int add(Resource document, String name) {
    List<Document> pages = new TikaDocumentReader(document).get();
    pages.forEach(page -> page.getMetadata().put("source", name));
    List<Document> chunks = TokenTextSplitter.builder().build().apply(pages);
    store.add(chunks);
    
    return chunks.size();
  }

  /**
   * Streams the answer to a question. Each item carries the retrieved chunks in its context.
   */
  public Flux<ChatClientResponse> ask(String question) {
    return chat.prompt().user(question).stream().chatClientResponse();
  }

  /**
   * Returns the names of the documents the retriever handed to the model.
   */
  @SuppressWarnings("unchecked")
  public static List<String> sourcesOf(ChatClientResponse response) {
    List<Document> docs = (List<Document>) response.context()
        .getOrDefault(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT, List.of());
        
    return docs.stream().map(doc -> String.valueOf(doc.getMetadata().get("source"))).toList();
  }
}
