package com.example.quily.schedular;

import com.example.quily.services.ShortLinkService;
import com.example.quily.util.ShortLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
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

   @Scheduled(fixedDelay = ShortLinkUtil.Delay)
   public void scheduleTaskWithFixedDelay() {
      LocalDateTime now = LocalDateTime.now();
      shortLinkService.deleteShortLink(now).map(count -> {
         System.out.println("Deleted " + count + " expired short links at " + now);
         return count;
      }).doOnError(throwable -> System.err.println("Error deleting expired short links: " + throwable.getMessage())).subscribe();
   }
}
