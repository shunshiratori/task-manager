package com.example.taskManager.contoroller;

import com.example.taskManager.dto.request.ProjectCreateRequest;
import com.example.taskManager.dto.request.ProjectUpdateRequest;
import com.example.taskManager.dto.response.ProjectCreateResponse;
import com.example.taskManager.dto.response.ProjectFindResponse;
import com.example.taskManager.dto.response.ProjectResponse;
import com.example.taskManager.service.ProjectService;
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
    public ProjectCreateResponse create(@RequestBody ProjectCreateRequest projects) {
        return projectService.create(projects);
    }

    @GetMapping
    public List<ProjectResponse> get(@RequestParam(required = false) Integer userId) {
            return projectService.get(userId);
    }

    @GetMapping("/{projectId}")
    public ProjectFindResponse find(@PathVariable("projectId") int id) {
        return projectService.find(id);
    }

    @PutMapping("/{projectId}")
    public ProjectResponse update(@PathVariable("projectId") int id, @RequestBody ProjectUpdateRequest project) {
        return projectService.update(id,project);
    }

    @DeleteMapping("/{projectId}")
    public void delete(@PathVariable("projectId") int id) {
        projectService.delete(id);
    }
}
