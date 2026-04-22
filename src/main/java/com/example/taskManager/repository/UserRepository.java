package com.example.taskManager.repository;

import com.example.taskManager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByMail(String email);
}
