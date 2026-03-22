package com.example.taskManager.dto;

import lombok.Data;

@Data
public class TaskCreateRequest {
    private String title;
    private String content;
    private Integer projectId;
}
