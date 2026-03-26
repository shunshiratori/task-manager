package com.example.taskManager.contoroller;

import com.example.taskManager.dto.request.UserCreateRequest;
import com.example.taskManager.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void create(@RequestBody UserCreateRequest userCreateRequest) {
        userService.create(userCreateRequest);
    }
}
