package com.tempmail.controller;

import com.tempmail.dto.InboxDto;
import com.tempmail.service.InboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inbox")
@RequiredArgsConstructor
public class InboxController {

    private final InboxService inboxService;

    @PostMapping("/create")
    public InboxDto createInbox() {
        return inboxService.createInbox();
    }

    @GetMapping("/{email}")
    public InboxDto getInbox(@PathVariable String email) {
        return inboxService.getInbox(email);
    }
}