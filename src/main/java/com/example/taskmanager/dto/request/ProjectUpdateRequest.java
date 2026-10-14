package com.example.taskmanager.dto.request;

import lombok.Data;

@Data
public class ProjectUpdateRequest {
    private String title;
    private int status;
    private int authorityId;
}
