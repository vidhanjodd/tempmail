package com.tempmail.controller;

import com.tempmail.dto.InboxDto;
import com.tempmail.service.InboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class InboxController {

    private final InboxService inboxService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/create")
    public String createInbox(Model model) {
        InboxDto inbox = inboxService.createInbox();
        model.addAttribute("inbox", inbox);
        return "inbox";
    }

    @GetMapping("/inbox/{email}")
    public String viewInbox(@PathVariable String email, Model model) {
        InboxDto inbox = inboxService.getInbox(email);
        model.addAttribute("inbox", inbox);
        return "inbox";
    }
}