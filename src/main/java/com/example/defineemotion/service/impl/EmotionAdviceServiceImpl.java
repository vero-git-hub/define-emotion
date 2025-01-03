package com.example.defineemotion.service.impl;

import com.example.defineemotion.service.EmotionAdviceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class EmotionAdviceServiceImpl implements EmotionAdviceService {

    private Map<String, String> adviceMap;

    public EmotionAdviceServiceImpl() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            adviceMap = objectMapper.readValue(
                    new ClassPathResource("emotion-advice.json").getInputStream(),
                    new TypeReference<Map<String, String>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load emotion advice from file.", e);
        }
    }

    /**
     * Retrieves advice for the given emotion.
     * @param emotion the emotion to retrieve advice for
     * @return the advice for the given emotion
     */
    @Override
    public String getAdvice(String emotion) {
        return adviceMap.getOrDefault(emotion.toLowerCase(), "No advice available for this emotion.");
    }
}