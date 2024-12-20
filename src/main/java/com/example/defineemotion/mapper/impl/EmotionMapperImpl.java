package com.example.defineemotion.mapper.impl;

import org.springframework.stereotype.Component;

import com.example.defineemotion.dto.EmotionRequestDto;
import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.mapper.EmotionMapper;
import com.example.defineemotion.model.Emotion;

@Component
public class EmotionMapperImpl implements EmotionMapper{

    @Override
    public EmotionResponseDto toDto(Emotion emotion) {
        return new EmotionResponseDto(
            emotion.getId(),
            emotion.getText(),
            emotion.getMood(),
            emotion.getTimestamp()
        );
    }

    @Override
    public Emotion toEntity(EmotionRequestDto dto) {
        Emotion emotion = new Emotion();
        emotion.setText(dto.text());
        emotion.setMood(dto.mood());
        return emotion;
    }
}
