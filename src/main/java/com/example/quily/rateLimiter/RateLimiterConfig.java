package com.example.quily.rateLimiter;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RateLimiterConfig {
   private Map<String, RateLimiterWindowState> rateLimiterContainer = new HashMap<>();
   Long nanoSeconds = 1_000_000_000L;

   public RateLimiterConfig() {
      rateLimiterContainer.put("likePreview", new RateLimiterWindowState(3600L * nanoSeconds, 8L, new ArrayDeque<>()));
   }

   public RateLimiterWindowState getRateLimiterState(String apiEndpoint) {
      return rateLimiterContainer.getOrDefault(apiEndpoint, new RateLimiterWindowState(nanoSeconds, 5L, new ArrayDeque<>()));
   }

   public void updateRateLimiterState(String apiEndpoint, RateLimiterWindowState newState) {
      rateLimiterContainer.put(apiEndpoint, newState);
   }
}
