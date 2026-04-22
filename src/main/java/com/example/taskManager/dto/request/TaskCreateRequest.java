package com.example.taskManager.dto.request;

import lombok.Data;

@Data
public class TaskCreateRequest {
    private String title;
    private String content;
    private Long userId;
    private Integer projectId;
}
