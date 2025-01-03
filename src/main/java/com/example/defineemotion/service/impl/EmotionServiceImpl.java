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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {
    
    private final EmotionRepository emotionRepository;
    private final EmotionMapper emotionMapper;
    private final UserRepository userRepository;

    /**
     * Adds a new emotion to the database.
     * @param text the text of the emotion
     * @param mood the mood of the emotion
     * @param username the username of the user
     * @return an optional containing the emotion response DTO if the emotion was added successfully, empty otherwise
     */
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

    /**
     * Retrieves all emotions for the given user.
     * @param username the username of the user
     * @return an optional containing a list of emotion response DTOs if the user has emotions, empty otherwise
     */
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

    /**
     * Deletes an emotion by ID.
     * @param id the ID of the emotion to delete
     * @return true if the emotion was deleted successfully, false otherwise
     */
    @Override
    public boolean deleteEmotionById(Long id) {
        if (emotionRepository.existsById(id)) {
            emotionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all emotions for the given user.
     * @param username the username of the user
     * @return a list of emotion response DTOs
     */
    @Override
    public List<EmotionResponseDto> getEmotionsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return emotionRepository.findByUser(user).stream()
                .map(emotionMapper::toDto)
                .collect(Collectors.toList());
    }
}