package com.tempmail.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDto {

    private UUID id;
    private String sender;
    private String subject;
    private String body;
    private LocalDateTime receivedAt;
}