package co.com.bancolombia.mcpclient.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ChatGatewayAdapterTest {

  @Test
  void testRegex() {
    String input = "<think>reasoning</think>result";
    String filtered = input.replaceAll("(?s)<think>.*?</think>", "").trim();
    assertEquals("result", filtered);
  }

  @Test
  void testRegexWithNewLines() {
    String input = "<think>\nmulti-line\nreasoning\n</think>\nActual response";
    String filtered = input.replaceAll("(?s)<think>.*?</think>", "").trim();
    assertEquals("Actual response", filtered);
  }

  @Test
  void testRegexWithNoThinkBlock() {
    String input = "no reasoning here";
    String filtered = input.replaceAll("(?s)<think>.*?</think>", "").trim();
    assertEquals("no reasoning here", filtered);
  }
}
