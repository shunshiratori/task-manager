package com.example.taskManager.Util;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final String SECRET = "mysecretkeymysecretkeymysecretkey";
    private static final long EXPIRATION = 1000 * 60 * 60;

    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // トークン生成
    public static String generationToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // userId取得
    public static Long extractUserId(String token) {
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}
