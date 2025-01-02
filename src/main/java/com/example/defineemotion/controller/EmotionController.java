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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

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
            log.warn("Empty or null text provided.");
            return ResponseEntity.badRequest().body(Map.of("error", "Text is required"));
        }

        try {
            String mood = analyzeMood(text);
            return ResponseEntity.ok(Map.of("result", mood));
        } catch (Exception e) {
            log.error("Error analyzing text", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to analyze text"));
        }
    }

    private String analyzeMood(String text) {
        String apiUrl = "api-url";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("api-token");
    
        Map<String, String> body = Map.of("inputs", text);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
    
        RestTemplate restTemplate = new RestTemplate();
    
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
    
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Error response from Hugging Face API: {}", response.getBody());
                return "Error";
            }
    
            String responseBody = response.getBody();
            if (responseBody == null || responseBody.isEmpty()) {
                log.error("Empty response received from Hugging Face API");
                return "Error";
            }
    
            JSONArray rootArray = new JSONArray(responseBody);
            if (rootArray.isEmpty()) {
                log.error("Empty result array from Hugging Face API");
                return "Error";
            }
    
            JSONArray resultArray = rootArray.getJSONArray(0);
            if (resultArray.isEmpty()) {
                log.error("Empty emotions array from Hugging Face API");
                return "Error";
            }
    
            JSONObject topEmotion = resultArray.getJSONObject(0);
            String label = topEmotion.getString("label");
    
            return label;
    
        } catch (HttpServerErrorException e) {
            log.error("Error communicating with Hugging Face API: {}", e.getMessage());
            return "Error";
        } catch (JSONException e) {
            log.error("Error parsing JSON response: {}", e.getMessage());
            return "Error";
        } catch (Exception e) {
            log.error("Unexpected error during analysis: {}", e.getMessage());
            return "Error";
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