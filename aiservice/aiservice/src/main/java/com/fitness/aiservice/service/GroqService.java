package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GroqService {

    private final WebClient webClient;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.api.key}")
    private String groqApiKey;

    public GroqService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

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

        try {
            return webClient.post()
                    .uri(groqApiUrl)
                    .header("Content-Type", "application/json")
                    .headers(headers -> headers.setBearerAuth(groqApiKey))
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            System.err.println("Error calling Groq API: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            throw e;
        }
    }
}
