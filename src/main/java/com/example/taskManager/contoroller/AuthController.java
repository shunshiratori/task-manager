package com.example.taskManager.contoroller;

import com.example.taskManager.Util.JwtUtil;
import com.example.taskManager.dto.request.LoginRequest;
import com.example.taskManager.entity.UserEntity;
import com.example.taskManager.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final UserRepository userRepository;
    public AuthController (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        UserEntity user = userRepository.findByMail(request.mail);

        if (user == null) {
            throw new RuntimeException("ユーザーなし");
        }

        if (!user.getPassword().equals(request.password)) {
            throw new RuntimeException("パスワード不一致");
        }

        return JwtUtil.generationToken(user.getUserId());
    }
}
