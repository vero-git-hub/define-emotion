package com.example.defineemotion.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.mapper.EmotionMapper;
import com.example.defineemotion.model.Emotion;
import com.example.defineemotion.repository.EmotionRepository;
import com.example.defineemotion.service.EmotionService;

@Service
public class EmotionServiceImpl implements EmotionService {
    private final EmotionRepository emotionRepository;
    private final EmotionMapper emotionMapper;

    public EmotionServiceImpl(EmotionRepository emotionRepository, EmotionMapper emotionMapper) {
        this.emotionRepository = emotionRepository;
        this.emotionMapper = emotionMapper;
    }

    @Override
    public EmotionResponseDto addEmotion(String text, String mood) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be empty or null");
        }

        Emotion emotion = new Emotion();
        emotion.setText(text);
        emotion.setMood(mood);
        
        Emotion savedEmotion = emotionRepository.save(emotion);
        return emotionMapper.toDto(savedEmotion);
    }

    @Override
    public List<EmotionResponseDto> getAllEmotions() {
        return emotionRepository.findAll().stream()
            .map(emotionMapper::toDto)
            .collect(Collectors.toList());
    }
}
