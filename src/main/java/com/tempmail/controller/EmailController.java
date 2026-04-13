package com.tempmail.controller;

import com.tempmail.dto.EmailDto;
import com.tempmail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/test")
    public String addEmail(
            @RequestParam String email,
            @RequestParam String sender,
            @RequestParam String subject,
            @RequestParam String body
    ) {
        emailService.addTestEmail(email, sender, subject, body);
        return "Email added successfully";
    }

    @GetMapping("/{email}")
    public List<EmailDto> getEmails(@PathVariable String email) {
        return emailService.getEmails(email);
    }
}