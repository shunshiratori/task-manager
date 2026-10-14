package com.example.taskmanager.dto.request;

import lombok.Data;

@Data
public class TaskUpdateRequest {
    private String title;
    private String content;
    private int status;
    private Integer projectId;
}
