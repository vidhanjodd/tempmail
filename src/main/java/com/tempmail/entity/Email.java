package com.tempmail.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Email {

    @Id
    @GeneratedValue
    private UUID id;

    private String sender;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private LocalDateTime receivedAt;

    @ManyToOne
    @JoinColumn(name = "inbox_id")
    private Inbox inbox;
}