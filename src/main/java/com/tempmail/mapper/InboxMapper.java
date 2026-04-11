package com.tempmail.mapper;

import com.tempmail.dto.InboxDto;
import com.tempmail.entity.Inbox;

public class InboxMapper {

    public static InboxDto toDto(Inbox inbox) {
        return InboxDto.builder()
                .id(inbox.getId())
                .emailAddress(inbox.getEmailAddress())
                .expiryTime(inbox.getExpiryTime())
                .createdAt(inbox.getCreatedAt())
                .build();
    }
}