package com.tempmail.controller;

import com.tempmail.dto.EmailDto;
import com.tempmail.service.EmailService;
import com.tempmail.service.MailgunSignatureVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MailgunSignatureVerifier signatureVerifier;

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
    public List<EmailDto> getEmails(
            @PathVariable String email,
            @RequestParam String token
    ) {
        return emailService.getEmails(email, token);
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveEmail(
            @RequestParam(value = "sender",     required = false) String sender,
            @RequestParam(value = "subject",    required = false) String subject,
            @RequestParam(value = "body-plain", required = false) String body,
            @RequestParam(value = "recipient",  required = false) String recipient,
            @RequestParam(value = "timestamp",  required = false) String timestamp,
            @RequestParam(value = "token",      required = false) String token,
            @RequestParam(value = "signature",  required = false) String signature
    ) {
//        if (!signatureVerifier.isValid(timestamp, token, signature)) {
//            return ResponseEntity.ok("Invalid signature — ignored");
//        }

        emailService.receiveEmail(sender, subject, body, recipient);
        return ResponseEntity.ok("Email received");
    }
}