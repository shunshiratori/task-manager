package com.example.taskManager.service;

import com.example.taskManager.dto.request.UserCreateRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

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
        userService.create(request);

        UserEntity expected = new UserEntity();
        expected.setName("taro");
        expected.setMail("taro@gmail.com");
        expected.setPassword("password");

        verify(userRepository, times(1)).save(expected);
    }
}