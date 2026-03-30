package com.example.taskManager.service;

import com.example.taskManager.dto.request.ProjectCreateRequest;
import com.example.taskManager.dto.request.ProjectUpdateRequest;
import com.example.taskManager.dto.response.ProjectCreateResponse;
import com.example.taskManager.dto.response.ProjectFindResponse;
import com.example.taskManager.dto.response.ProjectResponse;
import com.example.taskManager.dto.response.TaskResponse;
import com.example.taskManager.entity.ProjectEntity;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.ProjectRepository;
import com.example.taskManager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository repository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public ProjectCreateResponse create(ProjectCreateRequest projects) {
        ProjectEntity entity = new ProjectEntity();
        entity.setTitle(projects.getTitle());
        entity.setStatus(projects.getStatus());
        if (entity.getUser() != null) {
            UserEntity user = userRepository.getReferenceById(projects.getUserId());
            entity.setUser(user);
        }
        repository.save(entity);

        ProjectCreateResponse response = new ProjectCreateResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        if (entity.getUser() != null) {
            response.setUserId(entity.getUser().getUserId());
        }
        response.setAuthorityId(entity.getAuthorityId());
        return response;
    }

    public List<ProjectResponse> get() {
        List<ProjectEntity> entity = repository.findAll();
        return entity.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ProjectResponse convertToDto(ProjectEntity entity) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        response.setAuthorityId(entity.getAuthorityId());
        return response;
    }

    public ProjectFindResponse find(int id) {
        ProjectEntity entity = repository.findByIdWithTasks(id).orElseThrow(()-> new RuntimeException("Project not fount"));

        ProjectFindResponse response = new ProjectFindResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());

        if(entity.getTasks() != null) {
            List<TaskResponse> tasks = entity.getTasks().stream()
                    .map(task -> {
                        TaskResponse t = new TaskResponse();
                        t.setId(task.getId());
                        t.setTitle(task.getTitle());
                        t.setContent(task.getContent());
                        t.setStatus(task.getStatus());
                        t.setProjectId(task.getProject().getProjectId());
                        return t;
                    }).toList();

            response.setTasks(tasks);
        }
        return response;
    }

    public ProjectResponse update(int id, ProjectUpdateRequest project) {
        ProjectEntity entity = repository.findById(id).orElseThrow(()-> new RuntimeException("Project not found"));

        entity.setTitle(project.getTitle());
        entity.setStatus(project.getStatus());
        entity.setAuthorityId(project.getAuthorityId());
        repository.save(entity);

        ProjectResponse response = new ProjectResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        response.setAuthorityId(entity.getAuthorityId());
        return response;
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

}
