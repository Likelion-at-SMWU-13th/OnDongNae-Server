package com.example.ondongnae.backend.member.controller;

import com.example.ondongnae.backend.global.response.CustomResponse;
import com.example.ondongnae.backend.member.dto.SignUpDto;
import com.example.ondongnae.backend.member.dto.RegisterStoreDto;
import com.example.ondongnae.backend.member.service.AuthService;
import com.example.ondongnae.backend.store.dto.DescriptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<CustomResponse<?>> signupUser(@Valid @RequestBody SignUpDto signUpDto) {;
        long id = authService.addUser(signUpDto);
        if (id == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomResponse.badRequest("비밀번호가 일치하지 않습니다"));
        }
        else if (id == -2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomResponse.badRequest("잘못된 전화번호 포맷입니다."));
        }
        else if (id == -3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomResponse.badRequest("이미 가입된 전화번호입니다."));
        }
        else if (id == -4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomResponse.badRequest("이미 존재하는 아이디입니다."));
        }
        else return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CustomResponse.created("유저가 등록되었습니다", id));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomResponse<?>> login(@RequestBody Map<String, String> loginDto) {
        String loginId = loginDto.get("id");
        String password = loginDto.get("password");

        if (loginId == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomResponse.badRequest("아이디와 비밀번호를 모두 입력하세요."));
        }

        boolean success = authService.login(loginId, password);

        if (success) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(CustomResponse.ok("로그인에 성공했습니다"));
        }
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomResponse.unauthorized("잘못된 아이디 또는 비밀번호입니다."));
    }

    @PostMapping("/signup/store")
    public ResponseEntity<CustomResponse<?>> registerStore(@Valid @ModelAttribute RegisterStoreDto registerStoreDto) {
        Long id = authService.addStore(registerStoreDto);
        if (id == -1L) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomResponse.serverError("위도/경도 변환에 실패했습니다."));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomResponse.created("가게가 등록되었습니다", id));
    }
}
