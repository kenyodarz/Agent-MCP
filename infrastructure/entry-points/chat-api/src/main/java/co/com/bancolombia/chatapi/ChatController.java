package co.com.bancolombia.chatapi;

import co.com.bancolombia.usecase.chat.AgentChatUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

  private final AgentChatUseCase useCase;

  @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<String> chat(@RequestBody String message) {
    log.info("POST /api/chat - Received message: {}", message);
    return useCase.chat(message)
        .doOnSuccess(response -> log.info("Sending response (length: {} chars)", response.length()))
        .doOnError(error -> log.error("Error processing chat request", error));
  }
}
