package com.example.defineemotion.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.model.Emotion;
import com.example.defineemotion.repository.EmotionRepository;

@Service
public class EmotionService {
    private final EmotionRepository emotionRepository;

    public EmotionService(EmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

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
