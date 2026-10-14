package com.example.taskmanager.service;

import com.example.taskmanager.util.JwtUtil;
import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.entity.UserEntity;
import com.example.taskmanager.exception.AuthenticationException;
import com.example.taskmanager.repository.UserRepository;
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

        return jwtUtil.generateToken(user.getUserId());
    }
}
