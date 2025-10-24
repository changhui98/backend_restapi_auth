package com.springboot.jwtlogin.global.service;

import com.springboot.jwtlogin.config.security.JwtTokenProvider;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;

    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue().set(
            "RT:" + userId,
            refreshToken,
            REFRESH_TOKEN_EXPIRE_TIME,
            TimeUnit.MILLISECONDS
        );
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get("RT:" + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("RT:" +userId);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return jwtTokenProvider.validateToken(refreshToken);
    }

}
