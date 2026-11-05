package com.webforj.builtwithwebforj.ghostai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.JavaScript;
import com.webforj.annotation.Routify;
import com.webforj.bundle.annotation.BundleEntry;
import java.util.Set;
import org.springframework.ai.mcp.McpToolFilter;
import org.springframework.ai.mcp.McpToolNamePrefixGenerator;

@SpringBootApplication
@EnableAsync
@Routify(packages = "com.webforj.builtwithwebforj.ghostai.views")
@BundleEntry("app.css")
@JavaScript(value = "https://cdn.jsdelivr.net/combine/npm/prismjs@1/prism.min.js,npm/prismjs@1/plugins/autoloader/prism-autoloader.min.js", top = true)
@AppProfile(name = "ghost:ai", shortName = "ghost:ai")
public class Application extends App {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * The assistant only needs the documentation tools. The other tools of the webforJ MCP server
   * carry schemas that Gemini rejects, so they never reach the model.
   */
  @Bean
  McpToolFilter documentationToolsOnly() {
    Set<String> allowed = Set.of("search_knowledge_base", "get_document");
    return (connection, tool) -> allowed.contains(tool.name());
  }

  /**
   * Keeps the tool names as the server declares them, so the system prompt can name them.
   */
  @Bean
  McpToolNamePrefixGenerator mcpToolNames() {
    return McpToolNamePrefixGenerator.noPrefix();
  }
}
