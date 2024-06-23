package com.example.quily.schedular;

import com.example.quily.constants.ShortLinkConstants;
import com.example.quily.services.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShortLinkDeletionScheduler {

   private final ShortLinkService shortLinkService;


   @Autowired
   public ShortLinkDeletionScheduler(ShortLinkService shortLinkService) {
      this.shortLinkService = shortLinkService;
   }

   @Scheduled(fixedDelay = ShortLinkConstants.Delay)
   public void scheduleTaskWithFixedDelay() {
      LocalDateTime now = LocalDateTime.now();
      shortLinkService.deleteShortLink(now).map(count -> {
         System.out.println("Deleted " + count + " expired short links at " + now);
         return count;
      }).doOnError(throwable -> System.err.println("Error deleting expired short links: " + throwable.getMessage())).subscribe();
   }
}
