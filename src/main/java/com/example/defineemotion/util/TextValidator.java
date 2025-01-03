package com.example.defineemotion.util;

import org.springframework.stereotype.Component;

@Component
public class TextValidator {
    
    /**
     * Validates the given text.
     * @param text the text to validate
     * @return true if the text is valid, false otherwise
     */
    public boolean isValid(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }

        String sanitizedText = text.replaceAll("[^a-zA-Z0-9 ]", "").trim();
        return sanitizedText.length() > 2;
    }
}