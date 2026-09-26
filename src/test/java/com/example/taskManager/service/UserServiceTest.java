package com.example.taskManager.service;

import com.example.taskManager.dto.request.UserCreateRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("正常系")
    void testCreate() {
        UserCreateRequest request = new UserCreateRequest();
        request.setName("taro");
        request.setMail("taro@gmail.com");
        request.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("password");

        userService.create(request);

        UserEntity expected = new UserEntity();
        expected.setName("taro");
        expected.setMail("taro@gmail.com");
        expected.setPassword("password");

        verify(userRepository, times(1)).save(expected);
    }

    // 保存値が平文と異なる
    @Test
    @DisplayName("パスワードを平文ではなくハッシュ化して保存する")
    void create_encodesPasswordBeforeSaving() {
        UserCreateRequest request = new UserCreateRequest();
        request.setName("shun");
        request.setMail("shun@gmail.com");
        request.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        userService.create(request);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());

        UserEntity savedUser = captor.getValue();

//        assertNotEquals("password", savedUser.getPassword());
//        assertTrue(passwordEncoder.matches("password", savedUser.getPassword()));
        assertEquals("encoded-password", savedUser.getPassword());
        verify(passwordEncoder).encode("password");

    }
}