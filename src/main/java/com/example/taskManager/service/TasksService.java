package com.example.taskManager.service;

import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TasksService {
    private final TaskRepository repository;

    public TasksService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskEntity create(TaskEntity tasks){
        return repository.save(tasks);
    }
}
