package com.tempmail.service;

import com.tempmail.dto.InboxDto;
import com.tempmail.entity.Inbox;
import com.tempmail.mapper.InboxMapper;
import com.tempmail.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;
    private static final int EXPIRY_MINUTES = 10;

    public InboxDto createInbox() {
        String email = generateRandomEmail();
        String token = generateAccessToken();

        Inbox inbox = Inbox.builder()
                .emailAddress(email)
                .accessToken(token)
                .createdAt(LocalDateTime.now())
                .expiryTime(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES))
                .build();

        return InboxMapper.toDto(inboxRepository.save(inbox));
    }

    public InboxDto getInbox(String email) {
        Inbox inbox = inboxRepository.findByEmailAddress(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inbox not found"));
        return InboxMapper.toDto(inbox);
    }

    public void deleteInbox(UUID id) {
        inboxRepository.deleteById(id);
    }

    private String generateRandomEmail() {
        return randomString(10) + "@mail.tmpbox.lol";
    }

    private String generateAccessToken() {
        return randomString(32);
    }

    private String randomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rng = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rng.nextInt(chars.length())));
        }
        return sb.toString();
    }
}