package com.example.quily.rateLimiter;

import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
   private final RateLimiterConfig rateLimiterConfig;

   public RateLimiterService(RateLimiterConfig rateLimiterConfig) {
      this.rateLimiterConfig = rateLimiterConfig;
   }

   public synchronized Boolean isRequestAllowed(String apiName) {
      final Long currentNanos = System.nanoTime();
      final RateLimiterWindowState windowState = rateLimiterConfig.getRateLimiterState(apiName);
      final RateLimiterWindowState updatedWindowState = windowState.removeExpiredRequest(windowState.getWindow(), currentNanos);

      if (updatedWindowState.getWindow().size() < updatedWindowState.getLimit()) {
         RateLimiterWindowState finalState = updatedWindowState.addRequest();
         rateLimiterConfig.updateRateLimiterState(apiName, finalState);
         return true;
      }
      else return false;
   }
}
