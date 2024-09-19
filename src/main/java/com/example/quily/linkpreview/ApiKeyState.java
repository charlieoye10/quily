package com.example.quily.linkpreview;

import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicLong;

import static com.example.quily.linkpreview.HeaderConstants.LINK_PREVIEW_API_KEYS;

@Configuration
public class ApiKeyState {
   private AtomicLong counter = new AtomicLong(0L);

   public Long getIndex() {
      Long currentValue = counter.getAndIncrement();
      return (currentValue % LINK_PREVIEW_API_KEYS.size());
   }
}
