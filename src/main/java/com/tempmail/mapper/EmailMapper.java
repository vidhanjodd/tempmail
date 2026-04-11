package com.tempmail.mapper;

import com.tempmail.dto.EmailDto;
import com.tempmail.entity.Email;

public class EmailMapper {

    public static EmailDto toDto(Email email) {
        return EmailDto.builder()
                .id(email.getId())
                .sender(email.getSender())
                .subject(email.getSubject())
                .body(email.getBody())
                .receivedAt(email.getReceivedAt())
                .build();
    }
}