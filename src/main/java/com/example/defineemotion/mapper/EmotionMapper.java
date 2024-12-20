package com.example.defineemotion.mapper;

import com.example.defineemotion.dto.EmotionRequestDto;
import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.model.Emotion;

public interface EmotionMapper {
    EmotionResponseDto toDto(Emotion emotion);
    Emotion toEntity(EmotionRequestDto dto);
}
