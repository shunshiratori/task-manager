package com.example.taskManager.Util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
     HttpServletRequest httpRequest = (HttpServletRequest) request;
     String authHeader = httpRequest.getHeader("Authorization");
     if (authHeader != null && authHeader.startsWith("Bearer ")) {
         String token = authHeader.replace("Bearer ", "");

         try {
             Long userId = JwtUtil.extractUserId(token);
             request.setAttribute("userId", userId);
         } catch (Exception e) {
             throw new RuntimeException("トークン不正");
         }
     }
     chain.doFilter(request,response);
    }
}
