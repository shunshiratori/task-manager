package com.example.taskManager.dto;

import lombok.Data;

@Data
public class ProjectUpdateRequest {
    private String title;
    private int status;
    private int authorityId;
}
