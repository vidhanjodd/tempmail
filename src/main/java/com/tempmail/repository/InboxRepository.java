package com.tempmail.repository;

import com.tempmail.entity.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InboxRepository extends JpaRepository<Inbox, UUID> {

    Optional<Inbox> findByEmailAddress(String emailAddress);

    @Query("SELECT i FROM Inbox i WHERE i.expiryTime < :now")
    List<Inbox> findAllExpiredBefore(java.time.LocalDateTime now);
}