package com.tempmail.repository;

import com.tempmail.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {

    List<Email> findByInboxId(UUID inboxId);
}