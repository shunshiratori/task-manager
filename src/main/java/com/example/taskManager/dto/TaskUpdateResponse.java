package com.example.taskManager.dto;

import lombok.Data;

@Data
public class TaskUpdateResponse {
    private Integer id;
    private String title;
    private String content;
    private int status;
}
