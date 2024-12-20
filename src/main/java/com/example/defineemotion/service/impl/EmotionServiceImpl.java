package com.example.defineemotion.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.model.Emotion;
import com.example.defineemotion.repository.EmotionRepository;
import com.example.defineemotion.service.EmotionService;

@Service
public class EmotionServiceImpl implements EmotionService {
    private final EmotionRepository emotionRepository;

    public EmotionServiceImpl(EmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

    @Override
    public EmotionResponseDto addEmotion(String text, String mood) {
        Emotion emotion = new Emotion();
        emotion.setText(text);
        emotion.setMood(mood);
        Emotion savedEmotion = emotionRepository.save(emotion);
        return new EmotionResponseDto(
            savedEmotion.getId(),
            savedEmotion.getText(),
            savedEmotion.getMood(),
            savedEmotion.getTimestamp()
        );
    }

    @Override
    public List<EmotionResponseDto> getAllEmotions() {
        return emotionRepository.findAll().stream()
            .map(emotion -> new EmotionResponseDto(
                emotion.getId(),
                emotion.getText(),
                emotion.getMood(),
                emotion.getTimestamp()
            ))
            .collect(Collectors.toList());
    }
}
