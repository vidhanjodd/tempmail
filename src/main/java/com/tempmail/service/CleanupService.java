package com.tempmail.service;

import com.tempmail.entity.Inbox;
import com.tempmail.repository.EmailRepository;
import com.tempmail.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanupService {

    private final InboxRepository inboxRepository;
    private final EmailRepository emailRepository;

    @Transactional
    public void deleteExpiredInboxes() {
        List<Inbox> expired = inboxRepository.findAllExpiredBefore(LocalDateTime.now());

        if (expired.isEmpty()) {
            return;
        }

        for (Inbox inbox : expired) {
            emailRepository.deleteByInboxId(inbox.getId());
        }

        inboxRepository.deleteAll(expired);

        log.info("Cleanup complete — deleted {} expired inboxes", expired.size());
    }
}