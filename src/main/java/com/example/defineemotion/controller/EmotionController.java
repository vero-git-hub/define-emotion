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
import com.example.defineemotion.service.EmotionAdviceService;
import com.example.defineemotion.service.EmotionAnalysisService;
import com.example.defineemotion.service.EmotionService;
import com.example.defineemotion.util.TextValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/emotions")
@RequiredArgsConstructor
@Slf4j
public class EmotionController {
    
    private final EmotionService emotionService;
    private final EmotionAnalysisService emotionAnalysisService;
    private final EmotionAdviceService emotionAdviceService;
    private final TextValidator textValidator;

    /**
     * Shows the list of emotions for the authenticated user.
     * @param model the model to add attributes to
     * @return the view name
     */
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

    /**
     * Shows the form to add a new emotion.
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/input")
    public String showAddEmotionForm(Model model) {
        model.addAttribute("activePage", "add-emotion");
        return "crud/add-emotion";
    }

    /**
     * Analyzes the mood of the given text and returns the result.
     * @param request the request body containing the text to analyze
     * @return the result of the emotion analysis
     */
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeText(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        
        if (!textValidator.isValid(text)) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Invalid input. Please enter meaningful text with at least 3 characters."
            ));
        }

        try {
            String mood = emotionAnalysisService.analyzeMood(text);
            String advice = emotionAdviceService.getAdvice(mood);
            return ResponseEntity.ok(Map.of("result", mood, "advice", advice));
        } catch (Exception e) {
            log.error("Error analyzing text", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to analyze text"));
        }
    }

    /**
     * Saves the emotion to the database.
     * @param text the text of the emotion
     * @param emotionResult the mood of the emotion
     * @param redirectAttributes the redirect attributes to add messages to
     * @return the view name
     */
    @PostMapping("/input")
    public String saveEmotion(@RequestParam String text, @RequestParam(required = false) String emotionResult, RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null || "anonymousUser".equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "User must be authenticated");
            return "error"; 
        }

        try {
            String mood = emotionResult != null ? emotionResult : "neutral";
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

    /**
     * Deletes an emotion by ID.
     * @param id the ID of the emotion to delete
     * @param redirectAttributes the redirect attributes to add messages to
     * @return the view name
     */
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

    /**
     * Retrieves the emotion data for the chart.
     * @return the emotion data
     */
    @GetMapping("/chart-data")
    @ResponseBody
    public List<EmotionResponseDto> getEmotionChartData() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return emotionService.getEmotionsByUsername(username);
    }

    /**
     * Shows the emotion chart.
     * @param model the model to add attributes to
     * @return the view name
     */
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