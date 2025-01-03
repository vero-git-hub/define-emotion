package com.example.defineemotion.util;

import org.springframework.stereotype.Component;

@Component
public class TextValidator {
    
    public boolean isValid(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String sanitizedText = text.replaceAll("[^a-zA-Z0-9 ]", "").trim();
        return sanitizedText.length() > 2;
    }
}