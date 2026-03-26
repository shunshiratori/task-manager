package com.example.taskManager.dto.request;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String name;
    private String mail;
    private Long password;
}
