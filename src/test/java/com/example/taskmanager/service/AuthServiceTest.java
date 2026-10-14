package com.example.taskmanager.service;

import com.example.taskmanager.util.JwtUtil;
import com.example.taskmanager.dto.request.LoginRequest;
import com.example.taskmanager.entity.UserEntity;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.example.taskmanager.exception.AuthenticationException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;
    private final JwtUtil jwtUtil = new JwtUtil("test-only-secret-at-least-32-bytes-long");

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    // 正しいパスワードは通る
    @Test
    @DisplayName("正しいパスワードの場合はログインできる")
    void login_correctPassword_returnsToken() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setMail("shun@gmail.com");
        user.setPassword("password");

        when(userRepository.findByMail("shun@gmail.com")).thenReturn(user);
        when(passwordEncoder.matches("password", "password")).thenReturn(true);

        LoginRequest request = new LoginRequest();
        request.mail = "shun@gmail.com";
        request.password = "password";

        String token = authService.login(request);
        assertEquals(1L, jwtUtil.extractUserId(token));
    }

    // 誤ったパスワードは拒否する
    @Test
    @DisplayName("誤ったパスワードの場合はエラーが出る")
    void login_incorrectPassword_throwsAuthenticationException() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setMail("shun@gmail.com");
        user.setPassword("correct-password");

        when(userRepository.findByMail("shun@gmail.com")).thenReturn(user);
        when(passwordEncoder.matches("wrong-password", "correct-password")).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.mail = "shun@gmail.com";
        request.password = "wrong-password";

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> authService.login(request));

        assertEquals("メールアドレスまたはパスワードが正しくありません", exception.getMessage());

    }
}