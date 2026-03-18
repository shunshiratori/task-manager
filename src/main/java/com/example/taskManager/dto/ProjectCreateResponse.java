package com.example.taskManager.dto;

import lombok.Data;

@Data
public class ProjectCreateResponse {
    private int projectId;
    private String title;
    private int status;
    private int authorityId;
}
