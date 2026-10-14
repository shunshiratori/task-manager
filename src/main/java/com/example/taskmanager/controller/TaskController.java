package com.example.taskmanager.controller;

import com.example.taskmanager.dto.request.TaskCreateRequest;
import com.example.taskmanager.dto.request.TaskUpdateRequest;
import com.example.taskmanager.dto.response.TaskCreateResponse;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.dto.response.TaskUpdateResponse;
import com.example.taskmanager.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskCreateResponse create(@RequestBody TaskCreateRequest request) {
        return taskService.create(request);
    }

    @GetMapping
    public List<TaskResponse> get(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return taskService.get(userId);
    }

    @GetMapping("/{id}")
    public TaskResponse find(@PathVariable("id") int id, @RequestAttribute("userId") Long userId) {
        return taskService.find(id, userId);
    }

    @PutMapping("/{id}")
    public TaskUpdateResponse update(@PathVariable("id") int id, @RequestBody TaskUpdateRequest request, @RequestAttribute("userId") Long userId) {
        return taskService.update(id, request, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id, @RequestAttribute("userId")Long userId) {
        taskService.delete(id, userId);
    }
}
