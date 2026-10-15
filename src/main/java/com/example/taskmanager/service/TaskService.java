package com.example.taskmanager.service;

import com.example.taskmanager.dto.request.TaskCreateRequest;
import com.example.taskmanager.dto.request.TaskUpdateRequest;
import com.example.taskmanager.dto.response.TaskCreateResponse;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.dto.response.TaskUpdateResponse;
import com.example.taskmanager.entity.ProjectEntity;
import com.example.taskmanager.entity.TaskEntity;
import com.example.taskmanager.entity.UserEntity;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TaskCreateResponse create(TaskCreateRequest request){
        TaskEntity entity = new TaskEntity();
        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        if(request.getProjectId() != null) {
            ProjectEntity project = projectRepository.getReferenceById(request.getProjectId());
            entity.setProject(project);
        }
        if (request.getUserId() != null) {
            UserEntity user = userRepository.getReferenceById(request.getUserId());
            entity.setUser(user);
        }
        taskRepository.save(entity);

        TaskCreateResponse response = new TaskCreateResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        if (entity.getProject() != null) {
            response.setProjectId(entity.getProject().getProjectId());
        }
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getUserId());
        }
        return response;
    }

    public List<TaskResponse> get(Long userId) {
        requireUserId(userId);

        return taskRepository.findByUserUserId(userId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse find(int id,Long userId) {
        TaskEntity entity = findOwnedTask(id, userId);
        return convertToDto(entity);
    }

    @Transactional
    public TaskUpdateResponse update(int id, TaskUpdateRequest request,Long userId) {
        TaskEntity entity = findOwnedTask(id, userId);

        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setStatus(request.getStatus());

        if (request.getProjectId() != null) {
            ProjectEntity project = projectRepository.getReferenceById(request.getProjectId());
            entity.setProject(project);
        }
        taskRepository.save(entity);

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

    public void delete(int id, Long userId) {
        TaskEntity entity = findOwnedTask(id, userId);
        taskRepository.delete(entity);
    }

    private TaskEntity findOwnedTask(int id, Long userId) {
        requireUserId(userId);

        return taskRepository.findByIdAndUserUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found"
                ));
    }

    private void requireUserId(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public TaskResponse convertToDto(TaskEntity entity) {
        TaskResponse response = new TaskResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setStatus(entity.getStatus());

        if (entity.getProject() != null) {
            response.setProjectId(entity.getProject().getProjectId());
        }

        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getUserId());
        }

        return response;
    }

}
