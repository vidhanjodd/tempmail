package com.tempmail.controller;

import com.tempmail.entity.Email;
import com.tempmail.entity.Inbox;
import com.tempmail.repository.EmailRepository;
import com.tempmail.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailReceiveController {

    private final InboxRepository inboxRepository;
    private final EmailRepository emailRepository;

    @PostMapping("/api/receive")
    public ResponseEntity<Void> receive(
            @RequestParam("recipient") String recipient,
            @RequestParam("sender") String sender,
            @RequestParam(value = "subject", defaultValue = "") String subject,
            @RequestParam(value = "body-html", defaultValue = "") String bodyHtml,
            @RequestParam(value = "stripped-text", defaultValue = "") String bodyText
    ) {
        log.info("Incoming email: to={} from={} subject={}", recipient, sender, subject);

        String toAddress = extractEmail(recipient);

        inboxRepository.findByEmailAddress(toAddress).ifPresentOrElse(inbox -> {
            if (inbox.getExpiryTime().isAfter(LocalDateTime.now())) {
                String body = bodyHtml.isBlank() ? bodyText : bodyHtml;
                Email email = Email.builder()
                        .sender(sender)
                        .subject(subject)
                        .body(body)
                        .receivedAt(LocalDateTime.now())
                        .inbox(inbox)
                        .build();
                emailRepository.save(email);
                log.info("Email saved for inbox: {}", toAddress);
            } else {
                log.info("Inbox expired, dropping email for: {}", toAddress);
            }
        }, () -> log.warn("No inbox found for: {}", toAddress));

        return ResponseEntity.ok().build();
    }

    private String extractEmail(String raw) {
        if (raw.contains("<") && raw.contains(">")) {
            return raw.substring(raw.indexOf('<') + 1, raw.indexOf('>')).trim().toLowerCase();
        }
        return raw.trim().toLowerCase();
    }
}