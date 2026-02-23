package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GroqService groqService;

    public String generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = groqService.getAnswer(prompt);
        processAiResponse(activity, aiResponse);
        log.info("RESPONSE FROM GROQ (RAW): {}", aiResponse);
        return aiResponse;
    }

    private void processAiResponse(Activity activity, String aiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            // Extract Grok message content
            JsonNode contentNode = rootNode
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content");

            String jsonContent = contentNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("```", "")
                    .trim();

            log.info("PARSED RESPONSE FROM GROK (CLEAN JSON): {}", jsonContent);

            // OPTIONAL: If you want to convert it into JsonNode again
            JsonNode fitnessJson = mapper.readTree(jsonContent);

            // Now you can extract:
            // fitnessJson.path("analysis").path("overall").asText();

        } catch (Exception e) {
            log.error("Error parsing Grok response", e);
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
            You are a professional fitness AI coach.

            Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:

            {
              "analysis": {
                "overall": "Overall analysis here",
                "pace": "Pace analysis here",
                "heartRate": "Heart rate analysis here",
                "caloriesBurned": "Calories analysis here"
              },
              "improvements": [
                {
                  "area": "Area name",
                  "recommendation": "Detailed recommendation"
                }
              ],
              "suggestions": [
                {
                  "workout": "Workout name",
                  "description": "Detailed workout description"
                }
              ],
              "safety": [
                "Safety Point 1",
                "Safety Point 2"
              ]
            }

            Analyze this activity:

            Activity Type: %s
            Duration: %d minutes
            Calories Burned: %d
            Additional Metrics: %s

            IMPORTANT:
            - Return ONLY valid JSON.
            - Do NOT include markdown.
            - Do NOT include explanations before or after JSON.
            - Do NOT include backticks.
            - The response must be strictly valid parsable JSON.

            """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );
    }
}
