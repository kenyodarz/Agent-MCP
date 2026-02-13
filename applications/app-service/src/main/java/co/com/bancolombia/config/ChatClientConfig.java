package co.com.bancolombia.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class ChatClientConfig {

  @Bean
  public ChatClient chatClient(
      ChatClient.Builder chatClientBuilder,
      List<ToolCallbackProvider> toolCallbackProviders,
      @Value("${agent.system-prompt:#{null}}") String systemPrompt) {
    log.info("=== Configuring ChatClient ===");
    log.info("Found {} ToolCallbackProvider(s)", toolCallbackProviders.size());

    ChatClient.Builder builder = chatClientBuilder;

    // Collect all ToolCallbacks from providers
    List<ToolCallback> allTools = new ArrayList<>();
    for (ToolCallbackProvider provider : toolCallbackProviders) {
      ToolCallback[] tools = provider.getToolCallbacks();
      log.info("Provider has {} tools", tools.length);
      for (ToolCallback tool : tools) {
        allTools.add(tool);
        log.info("  - Registered tool: {}", tool.getToolDefinition().name());
      }
    }

    // Register all tools in ChatClient using the correct API
    if (!allTools.isEmpty()) {
      log.info("✅ Registering {} total MCP tools in ChatClient", allTools.size());
      // Pass individual ToolCallback objects, not the array
      for (ToolCallback tool : allTools) {
        builder = builder.defaultToolCallbacks(tool);
      }
    } else {
      log.warn("❌ No MCP tools found to register");
    }

    // Add system prompt if configured
    if (systemPrompt != null && !systemPrompt.isEmpty()) {
      log.info("✅ Configured system prompt");
      builder = builder.defaultSystem(systemPrompt);
    }

    ChatClient client = builder.build();
    log.info("=== ChatClient configuration complete ===");
    return client;
  }
}
