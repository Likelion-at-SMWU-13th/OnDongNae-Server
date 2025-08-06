package com.example.ondongnae.backend.global.config.security;

import com.example.ondongnae.backend.global.exception.JwtAuthenticationException;
import com.example.ondongnae.backend.member.model.Member;
import com.example.ondongnae.backend.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter  {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            if (token != null) {
                if (jwtProvider.validateToken(token)) {
                    // 인증 성공
                    String id = jwtProvider.getMemberIdFromToken(token);
                    Member member = memberRepository.findById(Long.valueOf(id))
                            .orElseThrow(() -> new RuntimeException("해당 ID의 사용자가 존재하지 않습니다."));

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(member, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new JwtAuthenticationException("JWT 인증에 실패했습니다.");
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            request.setAttribute("exceptionMessage", e.getMessage());
            throw e;
        }
    }

    private String resolveToken(HttpServletRequest request) {
        // 파싱해서 실제 토큰 부분만 추출
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
