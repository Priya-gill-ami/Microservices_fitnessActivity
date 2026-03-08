package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.api.key}")
    private String groqApiKey;

    public String getAnswer(String question) {

        System.out.println("API URL: " + groqApiUrl);
        System.out.println("API Key present: " + (groqApiKey != null && !groqApiKey.isEmpty()));
        System.out.println("Question: " + question);

        Map<String, Object> requestBody = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", question
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            groqApiUrl,
                            request,
                            String.class
                    );

            return response.getBody();

        } catch (Exception e) {

            System.err.println("Error calling Groq API: " + e.getMessage());

            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }

            throw e;
        }
    }
}