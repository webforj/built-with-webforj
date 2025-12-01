package com.webforj.builtwithwebforj.ghostai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

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

  public String predict(String input) {
    if (input == null || input.trim().length() < 10) return "";

    try {
      String result = chatClient.prompt().user(input).call().content();
      if (result == null || result.isBlank()) return "";

      result = result.trim();
      if (result.startsWith("\"") && result.endsWith("\"")) {
        result = result.substring(1, result.length() - 1);
      }

      // Check if result starts with input (case-insensitive for flexibility)
      if (result.toLowerCase().startsWith(input.toLowerCase()) && result.length() > input.length()) {
        // Return with original input preserved
        return input + result.substring(input.length());
      }
      return "";
    } catch (Exception e) {
      return "";
    }
  }
}
