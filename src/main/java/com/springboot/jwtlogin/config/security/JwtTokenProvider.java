package com.springboot.jwtlogin.config.security;

import com.springboot.jwtlogin.service.UserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    private final long tokenValidMillisecond = 1000L * 60 * 60;

    @Value("${jwt.secret}")
    private String secretKey = "secretKey";

    private SecretKey signingKey;

    @PostConstruct
    protected void init() {
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8));
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
    }

    public String createToken(String uid, List<String> roles) {
        log.info("[createToken] 토큰 생성시작]");
        Date now = new Date();

        String token = Jwts.builder()
            .subject(uid)
            .claims(Map.of("roles", roles))
            .issuedAt(now)
            .expiration(new Date(now.getTime() + tokenValidMillisecond))
            .signWith(signingKey)
            .compact();

        String bearerToken = "Bearer " + token;

        log.info("[createToken] 토큰 생성 완료");
        return bearerToken;
    }

    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails UserName : {}", userDetails.getUsername());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출");

        String pureToken = token.startsWith("Bearer ")
            ? token.substring(7)
            : token;

        String info = Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(pureToken)
            .getPayload()
            .getSubject();

        log.info("[getUsername] 토큰 기반 회원 구별 정보 추출 완료, info : {}", info);
        return info;
    }

    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효 체크 시작");
        try {
            Jws<Claims> claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);

            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
