package com.example.defineemotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.defineemotion.model.Emotion;
import com.example.defineemotion.service.EmotionService;

@RestController
@RequestMapping("/emotions")
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    public EmotionController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @PostMapping
    public Emotion addEmotion(@RequestParam String text, @RequestParam String mood) {
        return emotionService.addEmotion(text, mood);
    }

    @GetMapping
    public List<Emotion> getAllEmotions() {
        return emotionService.getAllEmotions();
    }
}
