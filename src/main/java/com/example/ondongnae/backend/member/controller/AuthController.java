package com.example.ondongnae.backend.member.controller;

import com.example.ondongnae.backend.global.response.CustomResponse;
import com.example.ondongnae.backend.member.dto.SignUpDto;
import com.example.ondongnae.backend.member.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup/user")
    public CustomResponse<?> signupUser(@Valid @RequestBody SignUpDto signUpDto) {;
        long id = authService.addUser(signUpDto);
        if (id == -1) {
            return CustomResponse.badRequest("비밀번호가 일치하지 않습니다");
        }
        else if (id == -2) {
            return CustomResponse.badRequest("잘못된 전화번호 포맷입니다.");
        }
        else if (id == -3) {
            return CustomResponse.badRequest("이미 가입된 전화번호입니다.");
        }
        else if (id == -4) {
            return CustomResponse.badRequest("이미 존재하는 아이디입니다.");
        }
        else return CustomResponse.created("유저가 등록되었습니다", id);
    }

    @PostMapping("/login")
    public CustomResponse<?> login(@RequestBody Map<String, String> loginDto) {
        String loginId = loginDto.get("id");
        String password = loginDto.get("password");

        if (loginId == null || password == null) {
            return CustomResponse.badRequest("아이디와 비밀번호를 모두 입력하세요.");
        }

        boolean success = authService.login(loginId, password);

        if (success) {
            return CustomResponse.ok("로그인에 성공했습니다");
        }
        else return CustomResponse.unauthorized("잘못된 아이디 또는 비밀번호입니다.");
    }
}
