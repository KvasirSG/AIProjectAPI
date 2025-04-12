package com.example.aiprojectapi.controller;

import com.example.aiprojectapi.model.ProjectRequest;
import com.example.aiprojectapi.model.ProjectResponse;
import com.example.aiprojectapi.service.ProjectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/generate")
    public Mono<ProjectResponse> generateProjects(@RequestBody ProjectRequest request) {
        return projectService.generateProjects(request);
    }
}

