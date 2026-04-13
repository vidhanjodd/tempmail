package com.tempmail.repository;

import com.tempmail.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {

    List<Email> findByInboxId(UUID inboxId);

    @Modifying
    @Query("DELETE FROM Email e WHERE e.inbox.id = :inboxId")
    void deleteByInboxId(@Param("inboxId") UUID inboxId);
}