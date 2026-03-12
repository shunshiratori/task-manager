package com.example.taskManager.contoroller;

import com.example.taskManager.dto.TaskCreateResponse;
import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.service.TasksService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    private final TasksService tasksService;

    public TaskController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping("/tasks")
    public TaskCreateResponse create(@RequestBody TaskEntity tasks) {
        return tasksService.create(tasks);
    }
}
