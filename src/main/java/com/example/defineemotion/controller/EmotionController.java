package com.example.defineemotion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.defineemotion.dto.EmotionResponseDto;
import com.example.defineemotion.service.EmotionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/emotions")
@RequiredArgsConstructor
@Slf4j
public class EmotionController {
    
    private final EmotionService emotionService;

    @GetMapping("/list")
    public String showEmotionList(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || "anonymousUser".equals(username)) {
            model.addAttribute("error", "User is not authenticated");
            return "error"; 
        }

        model.addAttribute("activePage", "view-emotions");
        model.addAttribute("emotions", emotionService.getAllEmotions(username).orElse(List.of()));
        
        return "/emotions/list";
    }

    @GetMapping("/input")
    public String showAddEmotionForm(Model model) {
        model.addAttribute("activePage", "add-emotion");
        return "crud/add-emotion";
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeText(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Text is required"));
        }

        String lowerText = analyzeMood(text);
        return ResponseEntity.ok(Map.of("result", lowerText));
    }

    private String analyzeMood(String text) {
        String lowerText = text.toLowerCase();

        if (lowerText.contains("happy") || lowerText.contains("great") || lowerText.contains("good")) {
            return "Happy";
        } else if (lowerText.contains("sad") || lowerText.contains("bad")) {
            return "Sad";
        } else {
            return "Neutral";
        }
    }

    @PostMapping("/input")
    public String saveEmotion(@RequestParam String text, @RequestParam(required = false) String emotionResult, RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || "anonymousUser".equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "User must be authenticated");
            return "error"; 
        }

        try {
            String mood = emotionResult != null ? emotionResult : "Neutral";
            Optional<EmotionResponseDto> addedEmotion = emotionService.addEmotion(text, mood, username);

            if (addedEmotion.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to save the emotion.");
                return "redirect:/emotions/input";
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/emotions/input";
        }

        return "redirect:/emotions/list";
    }

    @PostMapping("/delete")
    public String deleteEmotion(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = emotionService.deleteEmotionById(id);
        if (!isDeleted) {
            redirectAttributes.addFlashAttribute("errorMessage", "Emotion with ID " + id + " not found.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Emotion successfully deleted.");
        }
        return "redirect:/emotions/list";
    }

    @GetMapping("/chart-data")
    @ResponseBody
    public List<EmotionResponseDto> getEmotionChartData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return emotionService.getEmotionsByUsername(username);
    }

    @GetMapping("/chart")
    public String showEmotionChart(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || username.equals("anonymousUser")) {
            model.addAttribute("errorMessage", "User not authenticated");
            return "error";
        }

        List<EmotionResponseDto> emotionList = emotionService.getEmotionsByUsername(username);

        if (emotionList == null) {
            emotionList = List.of();
        }

        model.addAttribute("emotions", emotionList);
        model.addAttribute("activePage", "chart");
        return "/emotions/chart";
    }

}