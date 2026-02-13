package co.com.bancolombia.usecase.chat;

import co.com.bancolombia.model.chat.gateways.ChatGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AgentChatUseCase {

  private final ChatGateway chatGateway;

  public Mono<String> chat(String message) {
    return chatGateway.sendMessage(message);
  }
}
