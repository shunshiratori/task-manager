package com.example.taskmanager.dto.response;

import lombok.Data;

@Data
public class ProjectResponse {
    private int projectId;
    private String title;
    private int status;
    private int authorityId;
    private Long userId;
}
