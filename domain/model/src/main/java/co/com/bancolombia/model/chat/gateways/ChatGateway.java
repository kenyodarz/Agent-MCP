package co.com.bancolombia.model.chat.gateways;

import reactor.core.publisher.Mono;

public interface ChatGateway {
  Mono<String> sendMessage(String message);
}
