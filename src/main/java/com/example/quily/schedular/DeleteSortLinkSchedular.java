package com.example.quily.schedular;

import com.example.quily.services.ShortLinkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DeleteSortLinkSchedular {

    @Autowired
    ShortLinkServiceImpl shortLinkService;

    @Scheduled(fixedDelay = 300000)
    public void scheduleTaskWithFixedDelay() {
        LocalDateTime now = LocalDateTime.now();
        shortLinkService.deleteShortLink(now).map(count -> {
            System.out.println("Deleted " + count + " expired short links at " + now);
            return count;
        }).doOnError(throwable -> System.err.println("Error deleting expired short links: " + throwable.getMessage())).subscribe();
    }
}
