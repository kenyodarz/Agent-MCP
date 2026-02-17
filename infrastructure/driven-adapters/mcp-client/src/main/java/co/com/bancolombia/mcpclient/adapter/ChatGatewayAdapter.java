package co.com.bancolombia.mcpclient.adapter;

import co.com.bancolombia.model.chat.gateways.ChatGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class ChatGatewayAdapter implements ChatGateway {

  private final ChatClient chatClient;

  public ChatGatewayAdapter(ChatClient chatClient) {
    log.info("=== Initializing ChatGatewayAdapter ===");
    this.chatClient = chatClient;
    log.info("=== ChatClient configured (auto-configured by Spring AI with MCP tools) ===");
  }

  @Override
  public Mono<String> sendMessage(String message) {
    log.info("Received message: {}", message);
    return Mono.fromCallable(() -> {
      log.debug("Building chat prompt with user message");
      String response = chatClient.prompt()
          .user(message)
          .call()
          .content();
      log.info("Received response from AI model (length: {} chars)", response != null ? response.length() : 0);
      log.debug("Response content: {}", response);
      return filterReasoning(response);
    })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError(error -> log.error("Error during chat interaction", error));
  }

  private String filterReasoning(String response) {
    if (response == null) {
      return null;
    }
    // Remove <think>...</think> blocks (including newlines) using DOTALL mode (?s)
    String filtered = response.replaceAll("(?s)<think>.*?</think>", "").trim();
    if (filtered.length() != response.length()) {
      log.info("Reasoning block filtered out. Original length: {}, Filtered length: {}",
          response.length(), filtered.length());
    }
    return filtered;
  }
}
