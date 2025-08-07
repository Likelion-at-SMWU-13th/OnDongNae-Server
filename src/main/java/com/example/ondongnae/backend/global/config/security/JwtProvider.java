package com.example.ondongnae.backend.global.config.security;

import com.example.ondongnae.backend.member.dto.TokenDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {

    private final long EXPIRE_ACCESS = 1000 * 60 * 60 * 24; // 1일
    private final long EXPIRE_REFRESH = 1000 * 60 * 60 * 24 * 7; // 1주
    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenDto createTokens(Long id) {
        Date now = new Date();

        String accessToken = Jwts.builder().setSubject(String.valueOf(id)).setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRE_ACCESS)).signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        String refreshToken = Jwts.builder().setSubject(String.valueOf(id)).setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRE_REFRESH)).signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    public String getMemberIdFromToken(String token) {
        // Subject로 설정해놓은 id 가져오기
        return Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
