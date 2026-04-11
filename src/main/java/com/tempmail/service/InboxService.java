package com.tempmail.service;

import com.tempmail.dto.InboxDto;
import com.tempmail.entity.Inbox;
import com.tempmail.mapper.InboxMapper;
import com.tempmail.repository.InboxRepository;
import com.tempmail.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;

    private static final int EXPIRY_MINUTES = 10;

    public InboxDto createInbox() {

        String email = EmailUtil.generateRandomEmail();

        Inbox inbox = Inbox.builder()
                .emailAddress(email)
                .createdAt(LocalDateTime.now())
                .expiryTime(LocalDateTime.now().plusMinutes(EXPIRY_MINUTES))
                .build();

        return InboxMapper.toDto(inboxRepository.save(inbox));
    }

    public InboxDto getInbox(String email) {
        Inbox inbox = inboxRepository.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("Inbox not found"));

        return InboxMapper.toDto(inbox);
    }

    public void deleteInbox(UUID id) {
        inboxRepository.deleteById(id);
    }


}