package com.tempmail.repository;

import com.tempmail.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InboxRepository extends JpaRepository<Inbox, UUID> {

    Optional<Inbox> findByEmailAddress(String emailAddress);
}