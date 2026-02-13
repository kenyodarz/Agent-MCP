package co.com.bancolombia.mcpclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class OAuth2WebClientConfig {

  /**
   * OAuth2 Authorized Client Manager for client_credentials flow WITHOUT web
   * context.
   * Uses AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager instead of
   * DefaultReactiveOAuth2AuthorizedClientManager to avoid serverWebExchange
   * requirement.
   */
  @Bean
  public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
      ReactiveClientRegistrationRepository clientRegistrationRepository,
      ReactiveOAuth2AuthorizedClientService authorizedClientService) {

    log.info("=== Configuring OAuth2 Authorized Client Manager (Service-based) ===");

    ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder
        .builder()
        .clientCredentials()
        .build();

    AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
        clientRegistrationRepository, authorizedClientService);

    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

    log.info("✅ OAuth2 client_credentials flow configured (no web context required)");
    return authorizedClientManager;
  }

  /**
   * WebClient.Builder bean that Spring AI MCP will use automatically.
   * This builder includes OAuth2 support to send Authorization: Bearer <token>
   * header.
   */
  @Bean
  @ConditionalOnMissingBean
  public WebClient.Builder webClientBuilder(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
    log.info("=== Configuring WebClient.Builder with OAuth2 for MCP ===");

    ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
        authorizedClientManager);

    // Set default OAuth2 client registration for all MCP requests
    oauth2.setDefaultClientRegistrationId("mcp-server");

    WebClient.Builder builder = WebClient.builder()
        .filter(oauth2)
        .filter((request, next) -> {
          log.debug("MCP Request: {} {}", request.method(), request.url());
          return next.exchange(request)
              .doOnNext(response -> log.debug("MCP Response: {}", response.statusCode()));
        });

    log.info("✅ WebClient.Builder configured with OAuth2");
    log.info("   Registration ID: mcp-server");
    log.info("   Token will be sent as: Authorization: Bearer <token>");
    log.info("   Spring AI MCP will use this builder automatically");

    return builder;
  }

  @Bean
  @ConditionalOnMissingBean
  public ChatClient.Builder chatClientBuilder(org.springframework.ai.chat.model.ChatModel chatModel) {
    return ChatClient.builder(chatModel);
  }
}
