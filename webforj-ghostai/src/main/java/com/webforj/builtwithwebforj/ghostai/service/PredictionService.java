package com.webforj.builtwithwebforj.ghostai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {
  private static final Logger logger = System.getLogger(PredictionService.class.getName());

  private final ChatClient chatClient;

  public PredictionService(ChatModel chatModel) {
    this.chatClient = ChatClient.builder(chatModel)
        .defaultSystem("""
            You are an autocomplete assistant for a chat input field. Users are asking questions \
            about Java and webforJ (a Java web framework). Your job is to predict and complete \
            what they are typing.

            Rules:
            - Return the FULL text including the original input
            - Keep completions short (add 3-6 words max)
            - Your response must start with the EXACT input text
            - No quotes, no explanation, just the completed text
            - Focus on Java and webforJ related questions

            Examples:
            Input: "How do I"
            Output: How do I create a Button in webforJ

            Input: "What is the"
            Output: What is the best way to handle events

            Input: "Can you explain"
            Output: Can you explain routing in webforJ

            Input: "Show me"
            Output: Show me a login form example

            Input: "How to"
            Output: How to use FlexLayout in webforJ
            """)
        .build();
  }

  /**
   * Runs on a Spring task thread because the model call blocks. Completes with an empty string
   * when there is no usable prediction.
   */
  @Async
  public CompletableFuture<String> predict(String input) {
    return CompletableFuture.completedFuture(complete(input));
  }

  private String complete(String input) {
    if (input == null || input.trim().length() < 10) return "";

    try {
      String result = chatClient.prompt().user(input).call().content();
      logger.log(Level.DEBUG, "Prediction for \"{0}\": {1}", input, result);
      if (result == null || result.isBlank()) return "";

      result = result.trim();
      if (result.startsWith("\"") && result.endsWith("\"")) {
        result = result.substring(1, result.length() - 1);
      }

      // Check if result starts with input (case-insensitive for flexibility)
      if (result.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT))
          && result.length() > input.length()) {
        // Return with original input preserved
        return input + result.substring(input.length());
      }
      logger.log(Level.DEBUG, "Prediction discarded, it does not continue the input: {0}", result);
      return "";
    } catch (Exception e) {
      logger.log(Level.WARNING, "Prediction failed: {0}", e.getMessage());
      return "";
    }
  }
}
