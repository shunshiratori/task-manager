package com.example.taskmanager.dto.response;

import lombok.Data;

@Data
public class TaskCreateResponse {
    private Integer id;
    private String title;
    private String content;
    private Long userId;
    private Integer projectId;
}
