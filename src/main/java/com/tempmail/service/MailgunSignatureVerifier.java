package com.tempmail.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class MailgunSignatureVerifier {

    @Value("${mailgun.webhook.signing-key}")
    private String signingKey;

    public boolean isValid(String timestamp, String token, String signature) {
        if (timestamp == null || token == null || signature == null) {
            log.warn("Mailgun webhook missing signature fields");
            return false;
        }
        try {
            String data = timestamp + token;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));

            return hex.toString().equals(signature);

        } catch (Exception e) {
            log.error("Signature verification error", e);
            return false;
        }
    }
}