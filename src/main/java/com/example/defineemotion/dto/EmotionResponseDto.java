package com.example.defineemotion.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record EmotionResponseDto(Long id, String text, String mood, LocalDateTime timestamp) {
    public String formattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }
}