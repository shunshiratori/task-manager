package com.example.taskManager.service;

import com.example.taskManager.Util.JwtUtil;
import com.example.taskManager.dto.request.LoginRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 正しいパスワードは通る
    @Test
    @DisplayName("正しいパスワードの場合はログインできる")
    void login_acceptsCorrectPassword() {
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
        assertEquals(1L, JwtUtil.extractUserId(token));
    }

    // 誤ったパスワードは拒否する
    @Test
    @DisplayName("誤ったパスワードの場合はエラーが出る")
    void login_rejectsIncorrectPassword() {
        UserEntity user = new UserEntity();
        user.setUserId(1L);
        user.setMail("shun@gmail.com");
        user.setPassword("correct-password");

        when(userRepository.findByMail("shun@gmail.com")).thenReturn(user);
        when(passwordEncoder.matches("password", "password")).thenReturn(true);

        LoginRequest request = new LoginRequest();
        request.mail = "shun@gmail.com";
        request.password = "wrong-password";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.login(request));

        assertEquals("パスワード不一致", exception.getMessage());

    }
}