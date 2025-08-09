package com.example.ondongnae.backend.member.service;

import com.example.ondongnae.backend.global.config.security.JwtProvider;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.member.dto.RegisterStoreDto;
import com.example.ondongnae.backend.member.dto.SignUpDto;
import com.example.ondongnae.backend.member.dto.TokenDto;
import com.example.ondongnae.backend.member.model.Member;
import com.example.ondongnae.backend.member.repository.MemberRepository;
import com.example.ondongnae.backend.store.dto.DescriptionResponseDto;
import com.example.ondongnae.backend.store.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final StoreService storeService;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider, StoreService storeService) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.storeService = storeService;
    }
    
    public Map<String, Object> addUser(SignUpDto signUpDto) {
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
                .password(password1)
                .name(signUpDto.getName())
                .phone(signUpDto.getPhoneNum())
                .build();

        Member savedMember = memberRepository.save(member);

        TokenDto tokens = jwtProvider.createTokens(savedMember.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("memberId", savedMember.getId());
        data.put("tokens", tokens);
        return data;
    }

    public TokenDto login(String id, String password) {
        Member member = memberRepository.findByLoginId(id);

        if (member == null || !member.getPassword().equals(password))
            throw new BaseException(ErrorCode.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다");
        else {
            TokenDto tokens = jwtProvider.createTokens(member.getId());
            return tokens;
        }
    }

}
