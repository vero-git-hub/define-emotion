package com.example.defineemotion.service;

import com.example.defineemotion.dto.EmotionResponseDto;

import java.util.List;

public interface EmotionService {
    EmotionResponseDto addEmotion(String text, String mood);
    List<EmotionResponseDto> getAllEmotions();
}
