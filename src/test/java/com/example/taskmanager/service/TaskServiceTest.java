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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        expect.setProjectId(1);
        expect.setUserId(1L);

        assertEquals(expect, result);
        verify(taskRepository, times(1)).save(org.mockito.Mockito.any());
    }

    @Test
    void get() {
    }

    @Test
    @DisplayName("一覧が0件なら空リストを返す")
    void get_noTasks_returnsEmptyList() {
        // 準備
        Long userId = 1L;

        // リポジトリがからリストを返すように設定
        when(taskRepository.findByUserUserId(userId))
                .thenReturn(List.of());

        // 実際にテスト対象の処理を実行する
        List<TaskResponse> result = taskService.get(userId);

        //assertなどで確認
        assertTrue(result.isEmpty());
        verify(taskRepository).findByUserUserId(userId);
    }

    @Test
    @DisplayName("自分のTask一覧がDTOに変換されて返ること")
    void get_ownedTasks_returnsTaskList() {
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setUserId(userId);

        TaskEntity task = new TaskEntity();
        task.setId(1);
        task.setTitle("タスク");
        task.setContent("内容");
        task.setStatus(0);
        task.setUser(user);

        when(taskRepository.findByUserUserId(userId))
                .thenReturn(List.of(task));

        List<TaskResponse> result = taskService.get(userId);

        assertEquals(1, result.size());

        TaskResponse response = result.get(0);
        assertEquals(1, response.getId());
        assertEquals("タスク", response.getTitle());
        assertEquals("内容", response.getContent());
        assertEquals(0, response.getStatus());
        assertEquals(userId, response.getUserId());

        verify(taskRepository).findByUserUserId(userId);
    }

    @Test
    @DisplayName("一覧取得でuserIdがnullなら401")
    void requireUserId() {
        // 実際にテスト対象の処理を実行する
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> taskService.get(null)
        );

        //assertなどで確認
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        verifyNoInteractions(taskRepository);
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
    @DisplayName("単体取得で見つからなければ404")
    void find_missingTask_throwsNotFount() {
        int taskId = 1;
        Long userId = 1L;

        when(taskRepository.findByIdAndUserUserId(taskId, userId))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException .class,
                () -> taskService.find(taskId, userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    @DisplayName("単体取得でuserIdがnullなら401")
    void find_nullUserId_throwsUnauthorized() {
        // 実際にテスト対象の処理を実行する
        ResponseStatusException exception = assertThrows(
                ResponseStatusException .class,
                () -> taskService.get(null)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

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
    @DisplayName("更新で見つからなければ404・保存しない")
    void update_missingTask_throwsNotFoundAndDoesNotSave() {
        int taskId = 1;
        Long userId = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest();

        when(taskRepository.findByIdAndUserUserId(taskId, userId))
                .thenReturn(Optional.empty());


        ResponseStatusException exception = assertThrows(
                ResponseStatusException .class,
                () -> taskService.update(taskId, request, userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(taskRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    @DisplayName("更新でuserIdがnullなら401")
    void update_nullUserId_throwsUnauthorized() {
        int taskId = 1;
        TaskUpdateRequest request = new TaskUpdateRequest();
        // 実際にテスト対象の処理を実行する
        ResponseStatusException exception = assertThrows(
                ResponseStatusException .class,
                () -> taskService.update(taskId, request,null)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    }

    @Test
    @DisplayName("自分のTaskを削除する")
    void delete_ownedTask_deletesTask() {
        Long userId = 1L;
        int taskId = 1;

        TaskEntity task = new TaskEntity();
        task.setId(taskId);

        when(taskRepository.findByIdAndUserUserId(taskId,userId))
                .thenReturn(Optional.of(task));

        taskService.delete(taskId, userId);

        verify(taskRepository).findByIdAndUserUserId(taskId,userId);
        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("削除で見つからなければ404・削除しない")
    void delete_missingTask_throwsNotFoundAndDoesNotDelete() {
        int taskId = 1;
        Long userId = 1L;

        when(taskRepository.findByIdAndUserUserId(taskId, userId))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException .class,
                () -> taskService.delete(taskId, userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(taskRepository, never()).delete(any(TaskEntity.class));
    }

    @Test
    @DisplayName("削除でuserIdがnullなら401")
    void delete_nullUserId_throwsUnauthorized() {
        int taskId = 1;
        // 実際にテスト対象の処理を実行する
        ResponseStatusException exception = assertThrows(
                ResponseStatusException .class,
                () -> taskService.delete(taskId, null)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    }
}