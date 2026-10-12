package com.example.taskManager.repository;

import com.example.taskManager.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findByUserUserId(Long userId);
    Optional<TaskEntity> findByIdAndUserUserId(int id, Long userId);
}
