package com.example.taskManager.Util;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtFilter implements Filter {
    private final JwtUtil jwtUtil;

    public JwtFilter (JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
     HttpServletRequest httpRequest = (HttpServletRequest) request;
     HttpServletResponse httpResponse = (HttpServletResponse) response;

     String authHeader = httpRequest.getHeader("Authorization");

     if (authHeader != null && authHeader.startsWith("Bearer ")) {
         String token = authHeader.replace("Bearer ", "");

         try {
             Long userId = jwtUtil.extractUserId(token);
             request.setAttribute("userId", userId);
         } catch (JwtException | IllegalArgumentException e) {
             httpResponse.sendError(
                     HttpServletResponse.SC_UNAUTHORIZED,
                     "認証トークンが無効です"
             );
             return;
         }
     }
     chain.doFilter(request,response);
    }
}
