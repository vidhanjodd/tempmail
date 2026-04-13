package com.tempmail.util;

import com.tempmail.service.CleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanupScheduler {

    private final CleanupService cleanupService;

    @Scheduled(fixedRate = 60000)
    public void runCleanup() {
        System.out.println("Running cleanup job...");
        cleanupService.deleteExpiredInboxes();
    }
}