package com.example.taskManager.service;

import com.example.taskManager.dto.TaskCreateResponse;
import com.example.taskManager.dto.TaskResponse;
import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TasksService {
    private final TaskRepository repository;

    public TasksService(TaskRepository repository) {
        this.repository = repository;
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
        response.setProjectId(entity.getProjectId());
        response.setUserId(entity.getUserId());
        return response;
    }

    public TaskCreateResponse create(TaskEntity tasks){
        TaskEntity entity = repository.save(tasks);

        TaskCreateResponse response = new TaskCreateResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        return response;
    }
}
