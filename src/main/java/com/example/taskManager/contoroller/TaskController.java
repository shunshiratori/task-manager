package com.example.taskManager.contoroller;

import com.example.taskManager.dto.TaskCreateResponse;
import com.example.taskManager.dto.TaskResponse;
import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.service.TasksService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/tasks")
    public List<TaskResponse> get() {
        return tasksService.get();
    }

    @GetMapping("/tasks/{id}")
    public Optional<TaskResponse> find(@PathVariable("id") int id) {
        return tasksService.find(id);
    }
}
