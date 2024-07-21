package com.example.quily.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

   @Bean
   public Bucket createShortLinkBucket() {
      Refill refill = Refill.intervally(200, Duration.ofMinutes(1)); // 200 tokens per second
      Bandwidth limit = Bandwidth.classic(200, refill);
      return Bucket4j.builder().addLimit(limit).build();
   }

}

