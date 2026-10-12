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
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

    @Transactional
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

    public List<TaskResponse> get(Long userId) {
        requiredUserId(userId);

        return repository.findByUserUserId(userId)
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
    public TaskUpdateResponse update(int id, TaskUpdateRequest tasks,Long userID) {
        TaskEntity entity = findOwnedTask(id, userID);

        entity.setTitle(tasks.getTitle());
        entity.setContent(tasks.getContent());
        entity.setStatus(tasks.getStatus());

        if (tasks.getProjectId() != null) {
            ProjectEntity project = projectRepository.getReferenceById(tasks.getProjectId());
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

    public void delete(int id, Long userId) {
        TaskEntity entity = findOwnedTask(id, userId);
        repository.delete(entity);
    }

    private TaskEntity findOwnedTask(int id, Long userId) {
        requiredUserId(userId);

        return repository.findByIdAndUserUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not fount"
                ));
    }

    private void requiredUserId(Long userId) {
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
