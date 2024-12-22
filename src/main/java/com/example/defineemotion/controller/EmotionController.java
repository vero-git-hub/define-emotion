package com.example.defineemotion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.service.EmotionService;

import java.util.List;
import java.util.Optional;

@Controller
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    public EmotionController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @GetMapping("/emotions")
    public String showEmotionList(Model model) {
        String username = (String) model.getAttribute("username");

        if (username == null || username.isEmpty()) {
            model.addAttribute("error", "User is not authenticated");
            return "error";
        }

        model.addAttribute("activePage", "view-emotions");
        model.addAttribute("emotions", emotionService.getAllEmotions(username).orElse(List.of()));
        
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
            String username = (String) model.getAttribute("username");
            if (username == null) {
                throw new IllegalStateException("User must be authenticated");
            }

            String mood = analyzeMood(text);
            Optional<EmotionResponseDto> addedEmotion = emotionService.addEmotion(text, mood, username);

            if (addedEmotion.isEmpty()) {
                model.addAttribute("errorMessage", "Failed to save the emotion.");
                model.addAttribute("activePage", "add-emotion");
                return "add-emotion";
            }
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

    @PostMapping("/emotions/delete")
    public String deleteEmotion(@RequestParam Long id, Model model) {
        boolean isDeleted = emotionService.deleteEmotionById(id);
        if (!isDeleted) {
            model.addAttribute("errorMessage", "Emotion with ID " + id + " not found.");
        }
        return "redirect:/emotions";
    }
}