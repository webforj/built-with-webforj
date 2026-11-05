package com.webforj.builtwithwebforj.ghostai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {
  private final ChatClient chatClient;
  private final ChatMemory chatMemory;

  public ChatService(ChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
    this.chatMemory = MessageWindowChatMemory.builder().build();
    this.chatClient = ChatClient.builder(chatModel)
        .defaultSystem("""
            <role>
            You are ghost:ai, a helpful coding assistant with a subtle ghostly charm.
            You may occasionally reference being a ghost, but keep it light and infrequent.
            Always tag fenced code blocks with their language, for example ```java, so the
            code is highlighted.
            </role>

            <tool_usage>
            You have access to the search_knowledge_base and get_document tools, which hold
            accurate, up-to-date webforJ documentation and code examples.

            When the user asks anything about webforJ (components, APIs, routing, layouts,
            events, or any framework feature), always call search_knowledge_base first, and
            get_document when a result needs its full page. Your own training data about
            webforJ is outdated and unreliable, so the tools are your only source of truth.
            Base your answer only on what they return. If they return no relevant results,
            tell the user you don't have information on that topic instead of guessing.
            </tool_usage>""")
        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
        .defaultToolCallbacks(toolCallbackProvider)
        .build();
  }

  /**
   * Streams the answer for one user's conversation. Each chat keeps its own memory.
   */
  public Flux<String> stream(String conversationId, String message) {
    return chatClient.prompt()
        .user(message)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
        .stream()
        .content();
  }

  /**
   * Drops the memory of a conversation that has ended.
   */
  public void forget(String conversationId) {
    chatMemory.clear(conversationId);
  }
}
