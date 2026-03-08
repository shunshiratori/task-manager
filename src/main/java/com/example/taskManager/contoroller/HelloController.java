package com.example.taskManager.contoroller;

import com.example.taskManager.entity.TestTable;
import com.example.taskManager.repository.TestUserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    private final TestUserRepository repository;

    public HelloController(TestUserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/test")
    public TestTable hello(@RequestBody TestTable user) {
        return repository.save(user);
    }
}
