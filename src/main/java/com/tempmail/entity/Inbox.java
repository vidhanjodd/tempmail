package com.tempmail.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inbox {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String emailAddress;

    private LocalDateTime expiryTime;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "inbox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Email> emails;
}