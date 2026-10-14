package com.example.taskmanager.service;

import com.example.taskmanager.dto.request.ProjectCreateRequest;
import com.example.taskmanager.dto.request.ProjectUpdateRequest;
import com.example.taskmanager.dto.response.ProjectCreateResponse;
import com.example.taskmanager.dto.response.ProjectFindResponse;
import com.example.taskmanager.dto.response.ProjectResponse;
import com.example.taskmanager.dto.response.TaskResponse;
import com.example.taskmanager.entity.ProjectEntity;
import com.example.taskmanager.entity.UserEntity;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProjectCreateResponse create(ProjectCreateRequest request) {
        ProjectEntity entity = new ProjectEntity();
        entity.setTitle(request.getTitle());
        entity.setStatus(request.getStatus());
        if (request.getUserId() != null) {
            UserEntity user = userRepository.getReferenceById(request.getUserId());
            entity.setUser(user);
        }
        projectRepository.save(entity);

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

    public List<ProjectResponse> get(Long userId) {
            List<ProjectEntity> projects = userId != null
                    ? projectRepository.findByUserUserId(userId)
                    : projectRepository.findAll();
        return projects.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ProjectResponse convertToDto(ProjectEntity entity) {
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        response.setAuthorityId(entity.getAuthorityId());
        response.setUserId(entity.getUser().getUserId());
        return response;
    }

    @Transactional(readOnly = true)
    public ProjectFindResponse find(int id) {
        ProjectEntity entity = projectRepository.findByIdWithTasks(id).orElseThrow(()-> new RuntimeException("Project not found"));

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

    public ProjectResponse update(int id, ProjectUpdateRequest request) {
        ProjectEntity entity = projectRepository.findById(id).orElseThrow(()-> new RuntimeException("Project not found"));

        entity.setTitle(request.getTitle());
        entity.setStatus(request.getStatus());
        entity.setAuthorityId(request.getAuthorityId());
        projectRepository.save(entity);

        ProjectResponse response = new ProjectResponse();
        response.setProjectId(entity.getProjectId());
        response.setTitle(entity.getTitle());
        response.setStatus(entity.getStatus());
        response.setAuthorityId(entity.getAuthorityId());
        return response;
    }

    public void delete(int id) {
        projectRepository.deleteById(id);
    }

}
