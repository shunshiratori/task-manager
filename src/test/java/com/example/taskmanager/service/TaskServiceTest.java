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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("正常系: projectIdとuserIdがある場合")
    void create_withProjectAndUser_returnsCreatedTask() {
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);
        when(userRepository.getReferenceById(1L)).thenReturn(mockUser);

        ProjectEntity mockProject = new ProjectEntity();
        mockProject.setProjectId(1);
        when(projectRepository.getReferenceById(1)).thenReturn(mockProject);

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("タスク1");
        request.setContent("内容1");
        request.setProjectId(1);
        request.setUserId(1L);

        TaskCreateResponse result = taskService.create(request);

        TaskCreateResponse expect = new TaskCreateResponse();
        expect.setId(result.getId());
        expect.setTitle("タスク1");
        expect.setContent("内容1");
        expect.setProject_id(1);
        expect.setUserId(1L);

        assertEquals(expect, result);
        verify(taskRepository, times(1)).save(org.mockito.Mockito.any());
    }

    @Test
    void get() {
    }

    @Test
    @DisplayName("正常系: find")
    void find_ownedTask_returnsTask() {
        int taskId = 1;
        Long userId = 1L;

        TaskEntity task = new TaskEntity();
        task.setId(taskId);
        task.setTitle("タスク1");
        task.setContent("内容1");
        task.setStatus(0);

        ProjectEntity project = new ProjectEntity();
        project.setProjectId(1);
        task.setProject(project);

        UserEntity user = new UserEntity();
        user.setUserId(1L);
        task.setUser(user);

        when(taskRepository.findByIdAndUserUserId(taskId, userId))
                .thenReturn(Optional.of(task));

        TaskResponse result = taskService.find(taskId, userId);

        assertEquals(1, result.getId());
        assertEquals("タスク1", result.getTitle());
        assertEquals("内容1", result.getContent());
        assertEquals(0, result.getStatus());
        assertEquals(1, result.getProjectId());
        assertEquals(1L, result.getUserId());
    }

    @Test
    @DisplayName("正常系: update")
    void update_ownedTask_returnsUpdatedTask() {
        int taskId = 1;
        Long userId = 1L;

        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setTitle("古いタイトル");
        existingTask.setContent("古い内容");
        existingTask.setStatus(0);

        when(taskRepository.findByIdAndUserUserId(taskId, userId))
                .thenReturn(Optional.of(existingTask));

        ProjectEntity project = new ProjectEntity();
        project.setProjectId(2);
        when(projectRepository.getReferenceById(2)).thenReturn(project);

        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setTitle("新しいタイトル");
        updateRequest.setContent("新しい内容");
        updateRequest.setStatus(1);
        updateRequest.setProjectId(2);

        TaskUpdateResponse result = taskService.update(taskId, updateRequest, userId);

        assertEquals(1, result.getId());
        assertEquals("新しいタイトル", result.getTitle());
        assertEquals("新しい内容", result.getContent());
        assertEquals(1, result.getStatus());
        assertEquals(2, result.getProjectId());
    }

    @Test
    void delete() {
    }
}