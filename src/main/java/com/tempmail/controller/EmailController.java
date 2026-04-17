package com.tempmail.controller;

import com.tempmail.dto.EmailDto;
import com.tempmail.service.EmailService;
import com.tempmail.service.MailgunSignatureVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MailgunSignatureVerifier signatureVerifier;

    @GetMapping("/emails/{email}")
    public List<EmailDto> getEmails(
            @PathVariable String email,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;
        return emailService.getEmails(email, token);
    }

    @PostMapping({"/emails/receive", "/receive"})
    public ResponseEntity<String> receiveEmail(
            @RequestParam(value = "sender",     required = false) String sender,
            @RequestParam(value = "subject",    required = false) String subject,
            @RequestParam(value = "body-plain", required = false) String bodyPlain,
            @RequestParam(value = "body-html", required = false) String bodyHtml,
            @RequestParam(value = "stripped-text", required = false) String strippedText,
            @RequestParam(value = "recipient",  required = false) String recipient,
            @RequestParam(value = "timestamp",  required = false) String timestamp,
            @RequestParam(value = "token",      required = false) String token,
            @RequestParam(value = "signature",  required = false) String signature
    ) {
        if (!signatureVerifier.isValid(timestamp, token, signature)) {
            log.warn("Rejected webhook with invalid signature from recipient: {}", recipient);
            return ResponseEntity.ok("Invalid signature — ignored");
        }

        String body = firstPresent(bodyHtml, bodyPlain, strippedText);
        emailService.receiveEmail(sender, subject, body, recipient);
        return ResponseEntity.ok("Email received");
    }

    private String firstPresent(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
