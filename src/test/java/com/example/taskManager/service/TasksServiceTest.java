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

class TasksServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TasksService tasksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("正常系: projectIdとuserIdがある場合")
    void create() {
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

        TaskCreateResponse result = tasksService.create(request);

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
    void find() {
        int taskId = 1;

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

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Optional<TaskResponse> result = tasksService.find(taskId);

        assertEquals(1, result.get().getId());
        assertEquals("タスク1", result.get().getTitle());
        assertEquals("内容1", result.get().getContent());
        assertEquals(0, result.get().getStatus());
        assertEquals(1, result.get().getProjectId());
        assertEquals(1L, result.get().getUserId());
    }

    @Test
    @DisplayName("正常系: update")
    void update() {
        int taskId = 1;
        TaskEntity existingTask = new TaskEntity();
        existingTask.setId(taskId);
        existingTask.setTitle("古いタイトル");
        existingTask.setContent("古い内容");
        existingTask.setStatus(0);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        ProjectEntity project = new ProjectEntity();
        project.setProjectId(2);
        when(projectRepository.getReferenceById(2)).thenReturn(project);

        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setTitle("新しいタイトル");
        updateRequest.setContent("新しい内容");
        updateRequest.setStatus(1);
        updateRequest.setProjectId(2);

        TaskUpdateResponse result = tasksService.update(taskId, updateRequest);

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