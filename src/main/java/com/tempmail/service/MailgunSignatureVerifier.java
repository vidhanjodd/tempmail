package com.tempmail.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;

@Slf4j
@Service
public class MailgunSignatureVerifier {

    @Value("${mailgun.webhook.signing-key}")
    private String signingKey;

    private static final long TIMESTAMP_TOLERANCE_SECONDS = 300;

    public boolean isValid(String timestamp, String token, String signature) {
        if (timestamp == null || token == null || signature == null) {
            log.warn("Mailgun webhook missing signature fields");
            return false;
        }
        try {
            long webhookTime = Long.parseLong(timestamp);
            long now = Instant.now().getEpochSecond();
            if (Math.abs(now - webhookTime) > TIMESTAMP_TOLERANCE_SECONDS) {
                log.warn("Mailgun webhook timestamp too old or too far in future: {}", timestamp);
                return false;
            }
        } catch (NumberFormatException e) {
            log.warn("Mailgun webhook invalid timestamp: {}", timestamp);
            return false;
        }

        try {
            String data = timestamp + token;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] computedHash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            byte[] providedHash = hexToBytes(signature);

            return MessageDigest.isEqual(computedHash, providedHash);

        } catch (Exception e) {
            log.error("Signature verification error", e);
            return false;
        }
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}