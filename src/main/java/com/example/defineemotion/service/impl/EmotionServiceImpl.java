package com.example.defineemotion.service.impl;

import java.util.List;
import java.util.Optional;
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
    public Optional<EmotionResponseDto> addEmotion(String text, String mood) {
        if (text == null || text.trim().isEmpty()) {
            return Optional.empty();
        }

        Emotion emotion = new Emotion();
        emotion.setText(text);
        emotion.setMood(mood);

        Emotion savedEmotion = emotionRepository.save(emotion);
        return Optional.of(emotionMapper.toDto(savedEmotion));
    }

    @Override
    public Optional<List<EmotionResponseDto>> getAllEmotions() {
        List<EmotionResponseDto> emotions = emotionRepository.findAll().stream()
            .map(emotionMapper::toDto)
            .collect(Collectors.toList());
        return emotions.isEmpty() ? Optional.empty() : Optional.of(emotions);
    }
}
