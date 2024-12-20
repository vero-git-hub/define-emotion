package com.example.defineemotion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.defineemotion.dto.EmotionRequestDto;
import com.example.defineemotion.dto.EmotionResponseDto;
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
    public EmotionResponseDto addEmotion(@RequestBody EmotionRequestDto emotionRequestDto) {
        return emotionService.addEmotion(emotionRequestDto.text(), emotionRequestDto.mood());
    }

    @GetMapping
    public List<EmotionResponseDto> getAllEmotions() {
        return emotionService.getAllEmotions();
    }
}
