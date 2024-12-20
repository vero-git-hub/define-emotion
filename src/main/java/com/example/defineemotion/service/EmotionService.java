package com.example.defineemotion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.defineemotion.model.Emotion;
import com.example.defineemotion.repository.EmotionRepository;

@Service
public class EmotionService {
    private final EmotionRepository emotionRepository;

    public EmotionService(EmotionRepository emotionRepository) {
        this.emotionRepository = emotionRepository;
    }

    public Emotion addEmotion(String text, String mood) {
        Emotion emotion = new Emotion();
        emotion.setText(text);
        emotion.setMood(mood);
        return emotionRepository.save(emotion);
    }

    public List<Emotion> getAllEmotions() {
        return emotionRepository.findAll();
    }
}
