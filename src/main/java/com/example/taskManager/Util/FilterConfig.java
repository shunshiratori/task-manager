package com.example.taskManager.Util;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public Filter jwtFilter() {
        return new JwtFilter();
    }
}
