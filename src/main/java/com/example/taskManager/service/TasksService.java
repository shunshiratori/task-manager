package com.example.taskManager.service;

import com.example.taskManager.dto.TaskCreateResponse;
import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TasksService {
    private final TaskRepository repository;

    public TasksService(TaskRepository repository) {
        this.repository = repository;
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
