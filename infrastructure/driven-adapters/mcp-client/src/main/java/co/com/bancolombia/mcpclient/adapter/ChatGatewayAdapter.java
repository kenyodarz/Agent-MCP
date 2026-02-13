package co.com.bancolombia.mcpclient.adapter;

import co.com.bancolombia.model.chat.gateways.ChatGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class ChatGatewayAdapter implements ChatGateway {

  private final ChatClient chatClient;

  public ChatGatewayAdapter(
      ChatClient.Builder chatClientBuilder,
      @Value("${agent.system-prompt:#{null}}") String systemPrompt) {
    log.info("Initializing ChatGatewayAdapter with ChatClient.Builder");

    ChatClient.Builder builder = chatClientBuilder;

    // Add system prompt if configured
    if (systemPrompt != null && !systemPrompt.isEmpty()) {
      log.info("Configuring ChatClient with system prompt");
      builder = builder.defaultSystem(systemPrompt);
    }

    this.chatClient = builder.build();
    log.info("ChatClient configured successfully - MCP tools should be auto-registered");
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
      log.info("Received response from AI model (length: {} chars)", response.length());
      log.debug("Response content: {}", response);
      return response;
    })
        .subscribeOn(Schedulers.boundedElastic())
        .doOnError(error -> log.error("Error during chat interaction", error));
  }
}
