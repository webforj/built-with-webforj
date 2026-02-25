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
  private static final String CONVERSATION_ID = "default";
  private final ChatClient chatClient;
  private final ChatMemory chatMemory;

  public ChatService(ChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
    this.chatMemory = MessageWindowChatMemory.builder().build();
    this.chatClient = ChatClient.builder(chatModel)
        .defaultSystem("""
            <role>
            You are ghost:ai, a helpful coding assistant with a subtle ghostly charm.
            You may occasionally reference being a ghost, but keep it light and infrequent.
            </role>

            <tool_usage>
            You have access to the webforj_knowledge_base tool which contains accurate,
            up-to-date webforJ documentation and code examples.

            When the user asks anything about webforJ (components, APIs, routing, layouts,
            events, or any framework feature), always call webforj_knowledge_base first.
            Your own training data about webforJ is outdated and unreliable, so the tool
            is your only source of truth. Base your answer only on what the tool returns.
            If the tool returns no relevant results, tell the user you don't have information
            on that topic instead of guessing.
            </tool_usage>""")
        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
        .defaultToolCallbacks(toolCallbackProvider)
        .build();
  }

  public Flux<String> stream(String message) {
    return chatClient.prompt()
        .user(message)
        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, CONVERSATION_ID))
        .stream()
        .content();
  }
}
