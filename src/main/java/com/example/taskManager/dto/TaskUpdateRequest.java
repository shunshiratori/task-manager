package com.example.taskManager.dto;

import lombok.Data;

@Data
public class TaskUpdateRequest {
    private String title;
    private String content;
    private int status;
    private Integer projectId;
}
