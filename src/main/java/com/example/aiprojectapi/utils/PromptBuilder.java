package com.example.aiprojectapi.utils;

import com.example.aiprojectapi.model.ProjectRequest;

public class PromptBuilder {
    public static String buildPrompt(ProjectRequest request) {
        return String.format(
                "Suggest 3 software development project ideas for a %s in %s that can be completed in %s.",
                request.getSkillLevel(), request.getLanguage(), request.getTimeframe()
        );
    }
}

