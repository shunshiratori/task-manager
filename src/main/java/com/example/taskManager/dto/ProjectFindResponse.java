package com.example.taskManager.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectFindResponse {
    private int projectId;
    private String title;
    private int status;
    private int authorityId;
    private List<TaskResponse> tasks;
}
