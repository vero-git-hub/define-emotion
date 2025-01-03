package com.example.defineemotion.service.impl;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.defineemotion.service.EmotionAnalysisService;
import com.example.defineemotion.util.TextValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmotionAnalysisServiceImpl implements EmotionAnalysisService {

    @Value("${huggingface.emotion.api.url}")
    private String apiUrl;

    @Value("${huggingface.emotion.api.token}")
    private String apiToken;

    private final TextValidator textValidator;

    /**
     * Analyzes the mood of the given text using the Hugging Face emotion analysis API.
     * @param text the text to analyze
     * @return the mood of the text as a string
     * @throws IllegalArgumentException if the text is invalid
     */
    @Override
    public String analyzeMood(String text) {
        if (!textValidator.isValid(text)) {
            log.warn("Invalid input detected: {}", text);
            throw new IllegalArgumentException("Invalid input. Please enter meaningful text with at least 3 characters.");
        }

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, String>> request = createRequest(text, headers);
        RestTemplate restTemplate = new RestTemplate();
        int maxRetries = 5;
        int retryCount = 0;
        int waitTime = 5000;

        while (retryCount < maxRetries) {
            try {
                log.info("Attempt {} to analyze text using Hugging Face API.", retryCount + 1);
                ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

                if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                    log.warn("Model is currently loading. Waiting for {} ms before retrying...", waitTime);
                    Thread.sleep(waitTime);
                    retryCount++;
                    continue;
                }

                return processResponse(response);

            } catch (HttpServerErrorException | HttpClientErrorException e) {
                if (e.getStatusCode().value() == 503) {
                    log.warn("Model loading detected (503 Service Unavailable). Retrying...");
                    retryCount++;
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                        log.error("Interrupted while waiting to retry: {}", interruptedException.getMessage());
                        return "Error: Interrupted during retry process.";
                    }
                } else {
                    log.error("HTTP Error communicating with Hugging Face API: {} - {}", e.getStatusCode(), e.getMessage());
                    return "Error: Unable to process your request at the moment. Please try again later.";
                }
            } catch (JSONException e) {
                log.error("JSON Parsing Error: {}", e.getMessage());
                return "Error: Unable to parse the response from the emotion analysis service.";
            } catch (Exception e) {
                log.error("Unexpected Error during emotion analysis: {}", e.getMessage());
                return "Error: An unexpected error occurred. Please try again later.";
            }
        }

        log.error("Max retries reached. Unable to analyze text.");
        return "Error: The service is currently unavailable. Please try again later.";
    }

    /**
     * Creates the headers for the HTTP request.
     * @return the headers
     */
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);
        return headers;
    }

    /**
     * Creates the request entity for the HTTP request.
     * @param text the text to analyze
     * @param headers the headers for the request
     * @return the request entity
     */
    private HttpEntity<Map<String, String>> createRequest(String text, HttpHeaders headers) {
        Map<String, String> body = Map.of("inputs", text);
        return new HttpEntity<>(body, headers);
    }

    /**
     * Processes the response from the Hugging Face API.
     * @param response the response entity
     * @return the mood of the text as a string
     * @throws JSONException if the response cannot be parsed
     */
    private String processResponse(ResponseEntity<String> response) throws JSONException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Error: Non-successful status received. URL: {}, Status: {}, Response: {}", 
                      apiUrl, response.getStatusCode(), response.getBody());
            return "Error: The service could not process your request.";
        }

        String responseBody = response.getBody();
        if (responseBody == null || responseBody.isEmpty()) {
            log.error("Error: Empty response received. URL: {}", apiUrl);
            return "Error: No data received from the service.";
        }

        JSONArray rootArray = new JSONArray(responseBody);
        if (rootArray.isEmpty()) {
            log.error("Error: Empty root array in response. URL: {}", apiUrl);
            return "Error: The service returned no results.";
        }

        JSONArray resultArray = rootArray.optJSONArray(0);
        if (resultArray == null || resultArray.isEmpty()) {
            log.error("Error: Empty emotions array in response. URL: {}", apiUrl);
            return "Error: The service returned no emotion data.";
        }

        JSONObject topEmotion = resultArray.getJSONObject(0);
        return topEmotion.getString("label");
    }
}