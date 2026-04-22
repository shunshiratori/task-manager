package com.example.taskManager.service;

import com.example.taskManager.dto.request.ProjectCreateRequest;
import com.example.taskManager.dto.request.ProjectUpdateRequest;
import com.example.taskManager.dto.response.ProjectCreateResponse;
import com.example.taskManager.dto.response.ProjectFindResponse;
import com.example.taskManager.dto.response.ProjectResponse;
import com.example.taskManager.dto.response.TaskResponse;
import com.example.taskManager.entity.ProjectEntity;
import com.example.taskManager.entity.TaskEntity;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.ProjectRepository;
import com.example.taskManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("create:正常系")
    void create() {
        UserEntity mockUser = new UserEntity();
        mockUser.setUserId(1L);
        when(userRepository.getReferenceById(1L)).thenReturn(mockUser);

        ProjectCreateRequest request = new ProjectCreateRequest();
        request.setTitle("プロジェクト1");
        request.setStatus(0);
        request.setUserId(1L);

        // 実行
        ProjectCreateResponse result = projectService.create(request);

        ProjectCreateResponse expected = new ProjectCreateResponse();
        expected.setTitle("プロジェクト1");
        expected.setStatus(0);
        expected.setUserId(1L);

        // 検証
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("get: 正常_userIdがある場合")
    void get() {}

    @Test
    @DisplayName("get: 正常userIdがない場合")
    void get_notUserId() {}

    @Test
    @DisplayName("find: 正常系")
    void find() {
        int projectId = 1;

        ProjectEntity project = new ProjectEntity();
        project.setProjectId(projectId);
        project.setTitle("プロジェクト1");
        project.setStatus(0);

        TaskEntity task = new TaskEntity();
        task.setId(1);
        task.setTitle("タスク1");
        task.setContent("タスクの内容");
        task.setStatus(0);
        task.setProject(project);

        project.setTasks(List.of(task));

        when(projectRepository.findByIdWithTasks(projectId))
                .thenReturn(Optional.of(project));

        ProjectFindResponse result = projectService.find(projectId);

        TaskResponse expectedTask = new TaskResponse();
        expectedTask.setId(1);
        expectedTask.setTitle("タスク1");
        expectedTask.setContent("タスクの内容");
        expectedTask.setStatus(0);
        expectedTask.setProjectId(projectId);

        ProjectFindResponse expected = new ProjectFindResponse();
        expected.setProjectId(1);
        expected.setTitle("プロジェクト1");
        expected.setStatus(0);
        expected.setTasks(List.of(expectedTask));

        assertEquals(expected, result);
        verify(projectRepository, times(1)).findByIdWithTasks(projectId);
    }

    @Test
    @DisplayName("異常系: IDが存在しな場合")
    void findNotFound() {
        int projectId = 1;

        when(projectRepository.findByIdWithTasks(projectId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> projectService.find(projectId));
    }

    @Test
    @DisplayName("update:正常系")
    void update() {
        int projectId = 1;

        ProjectEntity existingProject = new ProjectEntity();
        existingProject.setProjectId(projectId);
        existingProject.setTitle("古いタイトル");
        existingProject.setStatus(0);
        existingProject.setAuthorityId(0);

        when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(existingProject));

        ProjectUpdateRequest updateRequest = new ProjectUpdateRequest();
        updateRequest.setTitle("新しいタイトル");
        updateRequest.setStatus(1);
        updateRequest.setAuthorityId(1);

        ProjectResponse result = projectService.update(projectId, updateRequest);

        ProjectResponse expected = new ProjectResponse();
        expected.setProjectId(1);
        expected.setTitle("新しいタイトル");
        expected.setStatus(1);
        expected.setAuthorityId(1);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("異常系: IDが存在しない場合")
    void updateNotFound() {
        int projectId = 1;

        when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> projectService.update(projectId, new ProjectUpdateRequest()));
    }

    @Test
    void delete() {
    }
}