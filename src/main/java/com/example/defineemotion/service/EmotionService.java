package com.example.defineemotion.service;

import com.example.defineemotion.dto.EmotionResponseDto;

import java.util.List;
import java.util.Optional;

public interface EmotionService {

    Optional<EmotionResponseDto> addEmotion(String text, String mood, String username);

    Optional<List<EmotionResponseDto>> getAllEmotions(String username);

    boolean deleteEmotionById(Long id);

    List<EmotionResponseDto> getEmotionsByUsername(String username);
}