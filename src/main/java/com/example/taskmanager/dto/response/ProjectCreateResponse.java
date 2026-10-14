package com.example.taskmanager.dto.response;

import lombok.Data;

@Data
public class ProjectCreateResponse {
    private int projectId;
    private String title;
    private int status;
    private Long userId;
    private int authorityId;
}
