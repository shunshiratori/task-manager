package com.example.taskmanager.controller;

import com.example.taskmanager.dto.request.ProjectCreateRequest;
import com.example.taskmanager.dto.request.ProjectUpdateRequest;
import com.example.taskmanager.dto.response.ProjectCreateResponse;
import com.example.taskmanager.dto.response.ProjectFindResponse;
import com.example.taskmanager.dto.response.ProjectResponse;
import com.example.taskmanager.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController (ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ProjectCreateResponse create(@RequestBody ProjectCreateRequest request) {
        return projectService.create(request);
    }

    @GetMapping
    public List<ProjectResponse> get(@RequestParam(required = false) Long userId) {
            return projectService.get(userId);
    }

    @GetMapping("/{projectId}")
    public ProjectFindResponse find(@PathVariable("projectId") int id) {
        return projectService.find(id);
    }

    @PutMapping("/{projectId}")
    public ProjectResponse update(@PathVariable("projectId") int id, @RequestBody ProjectUpdateRequest request) {
        return projectService.update(id, request);
    }

    @DeleteMapping("/{projectId}")
    public void delete(@PathVariable("projectId") int id) {
        projectService.delete(id);
    }
}
