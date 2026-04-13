package com.tempmail.service;

import com.tempmail.dto.EmailDto;
import com.tempmail.entity.Email;
import com.tempmail.entity.Inbox;
import com.tempmail.mapper.EmailMapper;
import com.tempmail.repository.EmailRepository;
import com.tempmail.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final InboxRepository inboxRepository;

    public void addTestEmail(String emailAddress, String sender, String subject, String body) {

        Inbox inbox = inboxRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new RuntimeException("Inbox not found"));

        Email email = Email.builder()
                .sender(sender)
                .subject(subject)
                .body(body)
                .receivedAt(LocalDateTime.now())
                .inbox(inbox)
                .build();

        emailRepository.save(email);
    }

    public List<EmailDto> getEmails(String emailAddress) {

        Inbox inbox = inboxRepository.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new RuntimeException("Inbox not found"));

        return emailRepository.findByInboxId(inbox.getId())
                .stream()
                .map(EmailMapper::toDto)
                .collect(Collectors.toList());
    }
}