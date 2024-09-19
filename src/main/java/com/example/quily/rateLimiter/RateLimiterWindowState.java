package com.example.quily.rateLimiter;

import lombok.Getter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@Getter
public class RateLimiterWindowState {
   private Long windowLength;
   private Long limit;
   private Deque<Long> window;

   public RateLimiterWindowState(Long windowLength, Long limit, Deque<Long> window) {
      this.windowLength = windowLength;
      this.limit = limit;
      this.window = new ConcurrentLinkedDeque<>(window);
   }

   public RateLimiterWindowState addRequest() {
      Long currentNanos = System.nanoTime();
      Deque<Long> updatedWindow = new ConcurrentLinkedDeque<>(this.window);
      updatedWindow.addLast(currentNanos);
      return new RateLimiterWindowState(this.windowLength, this.limit, updatedWindow);
   }

   public RateLimiterWindowState removeExpiredRequest(Deque<Long> currentWindow, Long currentNanos) {
      Deque<Long> newWindow = new ConcurrentLinkedDeque<>(currentWindow);

      while (!newWindow.isEmpty() && currentNanos - newWindow.peekFirst() > this.windowLength) {
         System.out.println("  --> " + currentNanos + " " + newWindow.peekFirst() + " " + (currentNanos - newWindow.peekFirst()) + " " + this.windowLength);
         newWindow.removeFirst();
      }

      return new RateLimiterWindowState(this.windowLength, this.limit, newWindow);
   }
}
