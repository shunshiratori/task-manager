package com.example.taskManager.dto.response;

import lombok.Data;

@Data
public class TaskCreateResponse {
    private Integer id;
    private String title;
    private String content;
    private Long userId;
    private Integer project_id;
}
