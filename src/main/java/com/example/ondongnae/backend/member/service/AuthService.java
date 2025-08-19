package com.example.ondongnae.backend.member.service;

import com.example.ondongnae.backend.global.config.security.JwtProvider;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.member.dto.SignUpDto;
import com.example.ondongnae.backend.member.dto.TokenDto;
import com.example.ondongnae.backend.member.model.Member;
import com.example.ondongnae.backend.member.model.RefreshToken;
import com.example.ondongnae.backend.member.repository.MemberRepository;
import com.example.ondongnae.backend.member.repository.RefreshTokenRepository;
import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Long addUser(SignUpDto signUpDto) {
        String password1 = signUpDto.getPassword1();
        String password2 = signUpDto.getPassword2();
        String phoneNum = signUpDto.getPhoneNum();
        String loginId = signUpDto.getLoginId();

        if (!password1.equals(password2)) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호가 일치하지 않습니다");
        }

        if (!phoneNum.matches("^010-\\d{4}-\\d{4}$")) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "잘못된 전화번호 포맷입니다.");
        }

        if (memberRepository.existsByPhone(phoneNum)) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 가입된 전화번호입니다.");
        }

        if (memberRepository.existsByLoginId(loginId)) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 아이디입니다.");
        }

        Member member = Member.builder()
                .loginId(signUpDto.getLoginId())
                .password(passwordEncoder.encode(password1))
                .name(signUpDto.getName())
                .phone(signUpDto.getPhoneNum())
                .build();

        Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    public TokenDto login(String id, String password) {
        Member member = memberRepository.findByLoginId(id);

        if (member == null || !passwordEncoder.matches(password, member.getPassword()))
            throw new BaseException(ErrorCode.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다");
        else {
            TokenDto tokens = createAndSaveToken(member.getId());
            return tokens;
        }
    }

    public Long getMyStoreId() {
        // 1. SecurityContext에서 인증 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 2. 인증 객체가 없거나 로그인하지 않은 경우 예외 발생
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BaseException(ErrorCode.UNAUTHORIZED);
        }

        // 3. principal에 담긴 Member 엔티티 가져오기 (JWT 필터에서 세팅)
        Member member = (Member) auth.getPrincipal();

        // 4. 해당 회원이 가진 Store ID 조회 (없으면 예외)
        return storeRepository.findByMemberId(member.getId())
                .map(Store::getId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND));
    }

    public TokenDto reissue(String refreshToken) {

        System.out.println(refreshToken);
        if (!jwtProvider.validateToken(refreshToken))
            throw new BaseException(ErrorCode.INVALID_TOKEN, "Refresh Token이 유효하지 않습니다.");

        Long memberId = Long.valueOf(jwtProvider.getMemberIdFromToken(refreshToken));

        RefreshToken savedRefreshToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.TOKEN_NOT_FOUND, "사용자 id에 해당하는 refresh token을 찾을 수 없습니다."));

        if (!savedRefreshToken.getRefreshToken().equals(refreshToken))
            throw new BaseException(ErrorCode.INVALID_TOKEN, "Refresh Token 값이 올바르지 않습니다.");

        TokenDto tokens = createAndSaveToken(memberId);

        return tokens;

    }

    @Transactional
    public TokenDto createAndSaveToken(Long memberId) {
        TokenDto tokens = jwtProvider.createTokens(memberId);

        refreshTokenRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        rt -> {
                            rt.updateRefreshToken(tokens.getRefreshToken());
                            refreshTokenRepository.save(rt);
                        }, () -> {
                            refreshTokenRepository.save(RefreshToken.builder().memberId(memberId).refreshToken(tokens.getRefreshToken()).build());
                        }
                );
        return tokens;
    }
}
