package com.tempmail.service;

import com.tempmail.dto.EmailDto;
import com.tempmail.entity.Email;
import com.tempmail.entity.Inbox;
import com.tempmail.mapper.EmailMapper;
import com.tempmail.repository.EmailRepository;
import com.tempmail.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final InboxRepository inboxRepository;

    public void addTestEmail(String emailAddress, String sender, String subject, String body) {
        Inbox inbox = inboxRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox not found"));

        Email email = Email.builder()
                .sender(sender)
                .subject(subject)
                .body(body)
                .receivedAt(LocalDateTime.now())
                .inbox(inbox)
                .build();

        emailRepository.save(email);
    }

    public List<EmailDto> getEmails(String emailAddress, String token) {
        Inbox inbox = inboxRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox not found"));

        if (!inbox.getAccessToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token");
        }

        if (inbox.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.GONE, "Inbox has expired");
        }

        return emailRepository.findByInboxId(inbox.getId())
                .stream()
                .map(EmailMapper::toDto)
                .collect(Collectors.toList());
    }

    public void receiveEmail(String sender, String subject, String body, String recipient) {
        if (recipient == null) {
            log.warn("Received email with no recipient — skipping");
            return;
        }

        Optional<Inbox> inboxOpt = inboxRepository.findByEmailAddress(recipient);
        if (inboxOpt.isEmpty()) {
            log.warn("Email received for unknown/expired inbox: {}", recipient);
            return;
        }

        Inbox inbox = inboxOpt.get();

        Email email = Email.builder()
                .sender(sender   != null ? sender   : "unknown")
                .subject(subject != null ? subject  : "(no subject)")
                .body(body       != null ? body      : "")
                .receivedAt(LocalDateTime.now())
                .inbox(inbox)
                .build();

        emailRepository.save(email);
    }
}