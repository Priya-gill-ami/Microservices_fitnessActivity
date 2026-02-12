package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class GrokService {

    private final WebClient webClient;

    @Value("${grok.api.url}")
    private String grokApiUrl;

    @Value("${grok.api.key}")
    private String grokApiKey;

    public GrokService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question) {

        Map<String, Object> requestBody = Map.of(
                "model", "grok-2-latest",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", question
                        )
                )
        );

        return webClient.post()
                .uri(grokApiUrl)
                .header("Content-Type", "application/json")
                .headers(headers -> headers.setBearerAuth(grokApiKey))
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
