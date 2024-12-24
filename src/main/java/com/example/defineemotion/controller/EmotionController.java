package com.example.defineemotion.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.service.EmotionService;

import java.util.List;
import java.util.Optional;

@Controller
public class EmotionController {

    private EmotionService emotionService;

    public EmotionController(EmotionService emotionService) {
        this.emotionService = emotionService;
    }

    @GetMapping("/emotions")
    public String showEmotionList(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || "anonymousUser".equals(username)) {
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
    public String processEmotionInput(@RequestParam String text, RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || "anonymousUser".equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "User must be authenticated");
            return "redirect:/emotions"; 
        }

        try {
            String mood = analyzeMood(text);
            Optional<EmotionResponseDto> addedEmotion = emotionService.addEmotion(text, mood, username);

            if (addedEmotion.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to save the emotion.");
                return "redirect:/emotions/input";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/emotions/input";
        }

        return "redirect:/emotions";
    }

    private String analyzeMood(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be empty");
        }

        String lowerText = text.toLowerCase();

        if (lowerText.contains("happy") || lowerText.contains("great") || lowerText.contains("good")) {
            return "Happy";
        } else if (lowerText.contains("sad") || lowerText.contains("bad")) {
            return "Sad";
        } else {
            return "Neutral";
        }
    }

    @PostMapping("/emotions/delete")
    public String deleteEmotion(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = emotionService.deleteEmotionById(id);
        if (!isDeleted) {
            redirectAttributes.addFlashAttribute("errorMessage", "Emotion with ID " + id + " not found.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Emotion successfully deleted.");
        }
        return "redirect:/emotions";
    }

    @GetMapping("/emotions/chart-data")
    @ResponseBody
    public List<EmotionResponseDto> getEmotionChartData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return emotionService.getEmotionsByUsername(username);
    }

    @GetMapping("/emotions/chart")
    public String showEmotionChart() {
        return "chart";
    }
}