package com.example.taskManager.service;

import com.example.taskManager.dto.ProjectCreateResponse;
import com.example.taskManager.dto.ProjectResponse;
import com.example.taskManager.entity.ProjectEntity;
import com.example.taskManager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public ProjectCreateResponse create(ProjectEntity projects) {
        ProjectEntity entity = repository.save(projects);

        ProjectCreateResponse response = new ProjectCreateResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        response.setAuthorityId(entity.getAuthorityId());
        return response;
    }

    public List<ProjectResponse> get() {
        List<ProjectEntity> entity = repository.findAll();
        return entity.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ProjectResponse convertToDto(ProjectEntity entity) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        response.setAuthorityId(entity.getAuthorityId());
        return response;
    }

    public ProjectResponse update(int id, ProjectEntity project) {
        ProjectEntity entity = repository.findById(id).orElseThrow(()-> new RuntimeException("Project not found"));

        entity.setTitle(project.getTitle());
        entity.setStatus(project.getStatus());
        entity.setAuthorityId(project.getAuthorityId());
        repository.save(entity);

        ProjectResponse response = new ProjectResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        response.setAuthorityId(entity.getAuthorityId());
        return response;
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

}
