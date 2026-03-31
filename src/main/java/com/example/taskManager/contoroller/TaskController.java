package com.example.taskManager.contoroller;

import com.example.taskManager.dto.request.TaskCreateRequest;
import com.example.taskManager.dto.request.TaskUpdateRequest;
import com.example.taskManager.dto.response.TaskCreateResponse;
import com.example.taskManager.dto.response.TaskResponse;
import com.example.taskManager.dto.response.TaskUpdateResponse;
import com.example.taskManager.service.TasksService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TasksService tasksService;

    public TaskController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @PostMapping
    public TaskCreateResponse create(@RequestBody TaskCreateRequest tasks) {
        return tasksService.create(tasks);
    }

    @GetMapping
    public List<TaskResponse> get(@RequestParam(required = false) Integer userId) {
        if (userId != null) {
            return tasksService.get(userId);
        }
        return tasksService.get();
    }

    @GetMapping("/{id}")
    public Optional<TaskResponse> find(@PathVariable("id") int id) {
        return tasksService.find(id);
    }

    @PutMapping("/{id}")
    public TaskUpdateResponse update(@PathVariable("id") int id, @RequestBody TaskUpdateRequest tasks) {
        return tasksService.update(id, tasks);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        tasksService.delete(id);
    }
}
