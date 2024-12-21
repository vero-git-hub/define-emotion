package com.example.defineemotion.service;

import com.example.defineemotion.dto.EmotionResponseDto;

import java.util.List;
import java.util.Optional;

public interface EmotionService {

    Optional<EmotionResponseDto> addEmotion(String text, String mood);

    Optional<List<EmotionResponseDto>> getAllEmotions();

    boolean deleteEmotionById(Long id);
}