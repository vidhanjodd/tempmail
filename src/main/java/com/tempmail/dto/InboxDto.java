package com.tempmail.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InboxDto {

    private UUID id;
    private String emailAddress;
    private String accessToken;
    private LocalDateTime expiryTime;
    private LocalDateTime createdAt;
}