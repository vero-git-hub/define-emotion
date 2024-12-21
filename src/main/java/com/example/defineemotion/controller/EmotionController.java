package com.example.defineemotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/emotions")
    public String showEmotionList(Model model) {
        model.addAttribute("activePage", "view-emotions");
        model.addAttribute("emotions", emotionService.getAllEmotions());
        return "emotions";
    }

    @GetMapping("/emotions/input")
    public String showAddEmotionForm(Model model) {
        model.addAttribute("activePage", "add-emotion");
        return "add-emotion";
    }

    @PostMapping("/emotions/input")
    public String processEmotionInput(@RequestParam String text, Model model) {
        try {
            String mood = analyzeMood(text);
            emotionService.addEmotion(text, mood);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("activePage", "add-emotion");
            return "add-emotion";
        }
        return "redirect:/emotions";
    }

    private String analyzeMood(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be empty");
        }

        if (text.contains("happy") || text.contains("great") || text.contains("good")) {
            return "Happy";
        } else if (text.contains("sad") || text.contains("bad")) {
            return "Sad";
        } else {
            return "Neutral";
        }
    }
    
}
