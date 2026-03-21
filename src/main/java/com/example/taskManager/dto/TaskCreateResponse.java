package com.example.taskManager.dto;

import lombok.Data;

@Data
public class TaskCreateResponse {
    private Integer id;
    private String title;
    private String content;
    private int project_id;
}
