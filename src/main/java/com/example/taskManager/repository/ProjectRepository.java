package com.example.taskManager.repository;

import com.example.taskManager.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    @Query("SELECT p FROM ProjectEntity p LEFT JOIN FETCH p.tasks WHERE p.projectId = :projectId")
    Optional<ProjectEntity> findByIdWithTasks(@Param("projectId") int projectId);
    List<ProjectEntity> findByUserUserId(Long userId);
}
