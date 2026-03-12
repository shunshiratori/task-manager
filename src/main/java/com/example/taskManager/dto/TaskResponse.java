package com.example.taskManager.dto;

import lombok.Data;

@Data
public class TaskResponse {
    private Integer id;
    private String title;
    private String content;
    private int status;
    private Integer projectId;
    private Integer userId;
}
