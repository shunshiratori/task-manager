package com.example.taskManager.service;

import com.example.taskManager.dto.request.UserCreateRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void create(UserCreateRequest userCreateRequest) {
        UserEntity entity = new UserEntity();
        entity.setName(userCreateRequest.getName());
        entity.setMail(userCreateRequest.getMail());
        entity.setPassword(userCreateRequest.getPassword());
        userRepository.save(entity);
    }
}
