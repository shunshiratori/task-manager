package com.example.taskmanager.dto.response;

import lombok.Data;

@Data
public class TaskResponse {
    private Integer id;
    private String title;
    private String content;
    private int status;
    private Integer projectId;
    private Long userId;
}
