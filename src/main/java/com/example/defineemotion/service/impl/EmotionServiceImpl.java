package com.example.defineemotion.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.mapper.EmotionMapper;
import com.example.defineemotion.model.Emotion;
import com.example.defineemotion.model.User;
import com.example.defineemotion.repository.EmotionRepository;
import com.example.defineemotion.repository.UserRepository;
import com.example.defineemotion.service.EmotionService;

@Service
public class EmotionServiceImpl implements EmotionService {
    private final EmotionRepository emotionRepository;
    private final EmotionMapper emotionMapper;
    private final UserRepository userRepository;

    public EmotionServiceImpl(EmotionRepository emotionRepository, EmotionMapper emotionMapper, UserRepository userRepository) {
        this.emotionRepository = emotionRepository;
        this.emotionMapper = emotionMapper;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<EmotionResponseDto> addEmotion(String text, String mood, String username) {
        if (text == null || text.trim().isEmpty()) {
            return Optional.empty();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Emotion emotion = new Emotion();
        emotion.setText(text);
        emotion.setMood(mood);
        emotion.setUser(user);

        Emotion savedEmotion = emotionRepository.save(emotion);
        return Optional.of(emotionMapper.toDto(savedEmotion));
    }

    @Override
    public Optional<List<EmotionResponseDto>> getAllEmotions(String username) {
        if (username == null || username.isEmpty()) {
            return Optional.empty();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
                
        List<EmotionResponseDto> emotions = emotionRepository.findByUser(user).stream()
                .map(emotionMapper::toDto)
                .collect(Collectors.toList());

        return emotions.isEmpty() ? Optional.empty() : Optional.of(emotions);
    }

    @Override
    public boolean deleteEmotionById(Long id) {
        if (emotionRepository.existsById(id)) {
            emotionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<EmotionResponseDto> getEmotionsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return emotionRepository.findByUser(user).stream()
                .map(emotionMapper::toDto)
                .collect(Collectors.toList());
    }
}