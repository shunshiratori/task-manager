package com.example.taskmanager.service;

import com.example.taskmanager.dto.request.UserCreateRequest;
import com.example.taskmanager.entity.UserEntity;
import com.example.taskmanager.repository.UserRepository;
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
