package com.example.taskManager.service;

import com.example.taskManager.dto.request.TaskCreateRequest;
import com.example.taskManager.dto.request.TaskUpdateRequest;
import com.example.taskManager.dto.response.TaskCreateResponse;
import com.example.taskManager.dto.response.TaskResponse;
import com.example.taskManager.dto.response.TaskUpdateResponse;
import com.example.taskManager.entity.ProjectEntity;
import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.ProjectRepository;
import com.example.taskManager.repository.TaskRepository;
import com.example.taskManager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TasksService {
    private final TaskRepository repository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TasksService(TaskRepository repository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TaskCreateResponse create(TaskCreateRequest tasks){
        TaskEntity entity = new TaskEntity();
        entity.setTitle(tasks.getTitle());
        entity.setContent(tasks.getContent());
        if(tasks.getProjectId() != null) {
            ProjectEntity project = projectRepository.getReferenceById(tasks.getProjectId());
            entity.setProject(project);
        }
        if (tasks.getUserId() != null) {
            UserEntity user = userRepository.getReferenceById(tasks.getUserId());
            entity.setUser(user);
        }
        repository.save(entity);

        TaskCreateResponse response = new TaskCreateResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        if (entity.getProject() != null) {
            response.setProject_id(entity.getProject().getProjectId());
        }
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getUserId());
        }
        return response;
    }

    public List<TaskResponse> get(Integer userId) {
        List<TaskEntity> entity = userId != null
                ? repository.findByUserUserId(userId)
                : repository.findAll();
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
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getUserId());
        }
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
                    if (entity.getUser() != null){
                        response.setUserId(entity.getUser().getUserId());
                    }
                    return response;
                });
    }

    public TaskUpdateResponse update(int id, TaskUpdateRequest tasks) {
        TaskEntity entity = repository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));

        entity.setTitle(tasks.getTitle());
        entity.setContent(tasks.getContent());
        entity.setStatus(tasks.getStatus());
        if (tasks.getProjectId() != null) {
            ProjectEntity project = projectRepository.getReferenceById(id);
            entity.setProject(project);
        }
        repository.save(entity);

        TaskUpdateResponse response = new TaskUpdateResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setStatus(entity.getStatus());
        if (entity.getProject() != null) {
            response.setProjectId(entity.getProject().getProjectId());
        }
        return response;
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

}
