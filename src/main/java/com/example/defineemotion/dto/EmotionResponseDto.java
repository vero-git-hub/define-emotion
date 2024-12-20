package com.example.defineemotion.dto;

import java.time.LocalDateTime;

public record EmotionResponseDto(Long id, String text, String mood, LocalDateTime timestamp) {

}
