package com.example.quily.linkpreview;

import com.example.quily.rateLimiter.RateLimiterService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.example.quily.linkpreview.HeaderConstants.*;

@Service
public class LinkPreviewService {
   private final WebClient webClient;
   private final ApiKeyState apiKeyState;
   private final RateLimiterService rateLimiterService;

   public LinkPreviewService(WebClient.Builder webClientBuilder, ApiKeyState apiKeyState, RateLimiterService rateLimiterService) {
      this.webClient = webClientBuilder.baseUrl("https://api.linkpreview.net").build();
      this.apiKeyState = apiKeyState;
      this.rateLimiterService = rateLimiterService;
   }

   private Mono<LinkPreviewResponse> getUrlDetailFromLikePreview(String url) {
      String apiKey = LINK_PREVIEW_API_KEYS.get(apiKeyState.getIndex().intValue());
      System.out.println("apiKey -> " + apiKey);
      return webClient.get()
         .uri(urlBuilder -> urlBuilder
            .queryParam("q", url)
            .build())
         .header(LINK_PREVIEW_API_KEY_HEADER, apiKey)
         .header("mode", "cors")
         .retrieve()
         .bodyToMono(LinkPreviewResponse.class)
         .onErrorResume(e -> Mono.just(new LinkPreviewResponse("Untitled",
            "", "", "",
            400, "Error found from LinkPreviewService")));
   }

   public Mono<LinkPreviewResponse> getUrlDetails(String url) {
      if (rateLimiterService.isRequestAllowed(RATE_LIMITER_CONTAINER_NAME)) {
         return getUrlDetailFromLikePreview(url);
      } else {
         return Mono.just(
            new LinkPreviewResponse("Untitled",
               "", "", "",
               HttpStatus.TOO_MANY_REQUESTS.value(),
               "API rate limit Exceeded on LinkPreviewService")
         );
      }
   }
}
