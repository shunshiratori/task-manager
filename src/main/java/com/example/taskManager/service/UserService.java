package com.example.taskManager.service;

import com.example.taskManager.dto.request.UserCreateRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void create(UserCreateRequest userCreateRequest) {
        UserEntity entity = new UserEntity();
        entity.setName(userCreateRequest.getName());
        entity.setMail(userCreateRequest.getMail());
        entity.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        userRepository.save(entity);
    }
}
