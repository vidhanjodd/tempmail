package com.tempmail.util;

import java.util.UUID;

public class EmailUtil {

    public static String generateRandomEmail() {
        String random = UUID.randomUUID().toString().substring(0, 6);
        return random + "@tempmail.com";
    }
}