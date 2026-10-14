package com.example.taskmanager.repository;

import com.example.taskmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByMail(String email);
}
