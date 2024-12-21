package com.example.defineemotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showHomePage(Model model) {
        model.addAttribute("activePage", "home");
        return "index";
    }

    @PostMapping
    public EmotionResponseDto addEmotion(@RequestBody EmotionRequestDto emotionRequestDto) {
        return emotionService.addEmotion(emotionRequestDto.text(), emotionRequestDto.mood());
    }

    @GetMapping("/emotions")
    public String showEmotionList(Model model) {
        model.addAttribute("activePage", "view-emotions");
        model.addAttribute("emotions", emotionService.getAllEmotions());
        return "emotions";
    }
}
