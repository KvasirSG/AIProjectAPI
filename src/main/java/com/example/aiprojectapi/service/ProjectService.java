package com.example.aiprojectapi.service;

import com.example.aiprojectapi.model.OpenAiResponse;
import com.example.aiprojectapi.model.ProjectRequest;
import com.example.aiprojectapi.model.ProjectResponse;
import com.example.aiprojectapi.utils.PromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    private final WebClient openAiWebClient;

    public ProjectService(WebClient openAiWebClient) {
        this.openAiWebClient = openAiWebClient;
    }

    public Mono<ProjectResponse> generateProjects(ProjectRequest request) {
        String prompt = PromptBuilder.buildPrompt(request);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        return openAiWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class) // map to a proper response object
                .map(responseJson -> {
                    // Parses GPT response, extract ideas
                    List<String> ideas = extractIdeas(responseJson);
                    return new ProjectResponse(ideas);
                });
    }

    private List<String> extractIdeas(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            OpenAiResponse response = mapper.readValue(json, OpenAiResponse.class);

            String content = response.getChoices().get(0).getMessage().getContent();

            // Split the content into individual ideas using newlines
            return Arrays.stream(content.split("\n"))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(line -> line.replaceAll("^\\d+\\.\\s*", "")) // Remove "1. ", "2. ", etc.
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return List.of("Failed to parse project ideas.");
        }
    }

}

