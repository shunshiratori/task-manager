package com.example.taskManager.contoroller;

import com.example.taskManager.Util.JwtUtil;
import com.example.taskManager.dto.request.TaskCreateRequest;
import com.example.taskManager.dto.request.TaskUpdateRequest;
import com.example.taskManager.dto.response.TaskCreateResponse;
import com.example.taskManager.dto.response.TaskResponse;
import com.example.taskManager.dto.response.TaskUpdateResponse;
import com.example.taskManager.service.TasksService;
import jakarta.servlet.http.HttpServletRequest;
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
    public List<TaskResponse> get(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return tasksService.get(userId);
    }

    @GetMapping("/{id}")
    public TaskResponse find(@PathVariable("id") int id, @RequestAttribute("userId") Long userId) {
        return tasksService.find(id, userId);
    }

    @PutMapping("/{id}")
    public TaskUpdateResponse update(@PathVariable("id") int id, @RequestBody TaskUpdateRequest tasks, @RequestAttribute("userId") Long userId) {
        return tasksService.update(id, tasks, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id, @RequestAttribute("userId")Long userId) {
        tasksService.delete(id, userId);
    }
}
