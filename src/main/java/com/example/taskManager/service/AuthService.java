package com.example.taskManager.service;

import com.example.taskManager.Util.JwtUtil;
import com.example.taskManager.dto.request.LoginRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.exception.ResourceNotFountException;
import com.example.taskManager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest request) {
        UserEntity user = userRepository.findByMail(request.mail);

        if (user == null) {
            throw new ResourceNotFountException("ユーザーなし");
        }

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new IllegalArgumentException("パスワード不一致");
        }

        return JwtUtil.generationToken(user.getUserId());
    }
}
