package com.liticia.soutenanceApp.utils;

import java.security.SecureRandom;

public class TokenGenerator {

    private static final String DIGITS = "0123456789";
    private static final int TOKEN_LENGTH = 6;

    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(index));
        }
        return sb.toString();
    }
}