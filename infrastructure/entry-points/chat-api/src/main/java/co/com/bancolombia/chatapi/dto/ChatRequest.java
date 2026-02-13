package co.com.bancolombia.chatapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ChatRequest {
  private String message;
}
