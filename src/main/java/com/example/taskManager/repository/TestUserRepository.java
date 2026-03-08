package com.example.taskManager.repository;

import com.example.taskManager.entity.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;

// entityをDBに保存したり取得する窓口
public interface TestUserRepository extends JpaRepository<TestTable, Integer> {
}
