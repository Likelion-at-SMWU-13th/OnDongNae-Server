package com.example.ondongnae.backend.member.service;

import com.example.ondongnae.backend.global.config.security.JwtProvider;
import com.example.ondongnae.backend.member.dto.RegisterStoreDto;
import com.example.ondongnae.backend.member.dto.SignUpDto;
import com.example.ondongnae.backend.member.dto.TokenDto;
import com.example.ondongnae.backend.member.model.Member;
import com.example.ondongnae.backend.member.repository.MemberRepository;
import com.example.ondongnae.backend.store.dto.DescriptionResponseDto;
import com.example.ondongnae.backend.store.service.StoreService;
import org.springframework.stereotype.Service;

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
    
    public long addUser(SignUpDto signUpDto) {
        String password1 = signUpDto.getPassword1();
        String password2 = signUpDto.getPassword2();
        String phoneNum = signUpDto.getPhoneNum();
        String loginId = signUpDto.getLoginId();

        if (!password1.equals(password2)) {
            return -1;
        }

        if (!phoneNum.matches("^010-\\d{4}-\\d{4}$")) {
            return -2;
        }

        if (memberRepository.existsByPhone(phoneNum)) {
            return -3;
        }

        if (memberRepository.existsByLoginId(loginId)) {
            return -4;
        }

        Member member = Member.builder()
                .loginId(signUpDto.getLoginId())
                .password(password1)
                .name(signUpDto.getName())
                .phone(signUpDto.getPhoneNum())
                .build();

        Member savedMember = memberRepository.save(member);

        TokenDto tokens = jwtProvider.createTokens(savedMember.getId());

        return savedMember.getId();
    }

    public boolean login(String id, String password) {
        System.out.println(id + password);
        Member member = memberRepository.findByLoginId(id);
        System.out.println(member);
        if (member == null) { return false; }
        if (!member.getPassword().equals(password)) { return false; }
        else { return true; }
    }

    public Long addStore(RegisterStoreDto registerStoreDto) {
        Long id = storeService.registerStore(registerStoreDto);
        return id;
    }
}
