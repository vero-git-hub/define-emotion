package com.example.defineemotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.defineemotion.dto.EmotionRequestDto;
import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.service.EmotionService;

@Controller
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    public EmotionController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @PostMapping
    public EmotionResponseDto addEmotion(@RequestBody EmotionRequestDto emotionRequestDto) {
        return emotionService.addEmotion(emotionRequestDto.text(), emotionRequestDto.mood());
    }

    @GetMapping("/emotions")
    public List<EmotionResponseDto> getAllEmotions() {
        return emotionService.getAllEmotions();
    }
}
