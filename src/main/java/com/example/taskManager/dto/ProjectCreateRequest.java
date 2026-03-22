package com.example.taskManager.dto;

import lombok.Data;

@Data
public class ProjectCreateRequest {
    private String title;
    private int status;
}
