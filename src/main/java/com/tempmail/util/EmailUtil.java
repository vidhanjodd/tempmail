package com.tempmail.util;

import java.security.SecureRandom;
import java.util.UUID;

public class EmailUtil {

    public static String generateRandomEmail() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom rng = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) sb.append(chars.charAt(rng.nextInt(chars.length())));
        return sb + "@tempmail.com";
    }
}