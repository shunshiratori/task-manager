package com.example.taskManager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String name;

    private String mail;

    private Long password;

    private int authorityId;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ProjectEntity> projects = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TaskEntity> tasks = new ArrayList<>();
}
