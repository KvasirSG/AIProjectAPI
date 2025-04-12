package com.example.aiprojectapi.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    private String language;
    private String skillLevel;
    private String timeframe;
}