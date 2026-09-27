package com.example.taskManager.service;

import com.example.taskManager.Util.JwtUtil;
import com.example.taskManager.dto.request.LoginRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.exception.AuthenticationException;
import com.example.taskManager.exception.ResourceNotFountException;
import com.example.taskManager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public AuthService (UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequest request) {
        UserEntity user = userRepository.findByMail(request.mail);

        if (user == null) {
            throw new AuthenticationException();
        }

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new AuthenticationException();
        }

        return jwtUtil.generationToken(user.getUserId());
    }
}
