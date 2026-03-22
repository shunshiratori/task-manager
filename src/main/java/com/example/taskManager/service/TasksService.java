package com.example.taskManager.service;

import com.example.taskManager.dto.TaskCreateRequest;
import com.example.taskManager.dto.TaskCreateResponse;
import com.example.taskManager.dto.TaskResponse;
import com.example.taskManager.dto.TaskUpdateResponse;
import com.example.taskManager.entity.ProjectEntity;
import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.repository.ProjectRepository;
import com.example.taskManager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TasksService {
    private final TaskRepository repository;
    private final ProjectRepository projectRepository;

    public TasksService(TaskRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }

    public TaskCreateResponse create(TaskCreateRequest tasks){
        TaskEntity entity = new TaskEntity();
        entity.setTitle(tasks.getTitle());
        entity.setContent(tasks.getContent());
        if(tasks.getProjectId() != null) {
            ProjectEntity project = projectRepository.getReferenceById(tasks.getProjectId());
            entity.setProject(project);
        }
        repository.save(entity);

        TaskCreateResponse response = new TaskCreateResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        if (entity.getProject() != null) {
            response.setProject_id(entity.getProject().getProjectId());
        }
        return response;
    }

    public List<TaskResponse> get() {
        List<TaskEntity> entity = repository.findAll();
        return entity.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public TaskResponse convertToDto(TaskEntity entity) {
        TaskResponse response = new TaskResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent((entity.getContent()));
        response.setStatus(entity.getStatus());
        if (entity.getProject() != null) {
            response.setProjectId(entity.getProject().getProjectId());
        }
        response.setUserId(entity.getUserId());
        return response;
    }

    public Optional<TaskResponse> find(int id) {
        return repository.findById(id)
                .map(entity -> {
                    TaskResponse response = new TaskResponse();
                    response.setId(entity.getId());
                    response.setTitle(entity.getTitle());
                    response.setContent(entity.getContent());
                    response.setStatus(entity.getStatus());
                    response.setProjectId(entity.getProject().getProjectId());
                    response.setUserId(entity.getUserId());
                    return response;
                });
    }

    public TaskUpdateResponse update(int id, TaskEntity tasks) {
        TaskEntity entity = repository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));

        entity.setTitle(tasks.getTitle());
        entity.setContent(tasks.getContent());
        entity.setStatus(tasks.getStatus());
        repository.save(entity);

        TaskUpdateResponse response = new TaskUpdateResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setStatus(entity.getStatus());
        return response;
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

}
