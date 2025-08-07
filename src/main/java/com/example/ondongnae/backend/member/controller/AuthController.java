package com.example.ondongnae.backend.member.controller;

import com.example.ondongnae.backend.category.dto.SignUpCategoryDto;
import com.example.ondongnae.backend.category.service.CategoryService;
import com.example.ondongnae.backend.global.response.CustomResponse;
import com.example.ondongnae.backend.member.dto.SignUpDto;
import com.example.ondongnae.backend.member.dto.RegisterStoreDto;
import com.example.ondongnae.backend.member.service.AuthService;
import com.example.ondongnae.backend.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final StoreService storeService;
    private final CategoryService categoryService;

    public AuthController(AuthService authService, StoreService storeService, CategoryService categoryService) {
        this.authService = authService;
        this.storeService = storeService;
        this.categoryService = categoryService;
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
        Long id = storeService.registerStore(registerStoreDto);
        if (id == -1L) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomResponse.serverError("위도/경도 변환에 실패했습니다."));
        } else if (id == -2L) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomResponse.serverError("AI로부터 응답을 받아오지 못했습니다."));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomResponse.created("가게가 등록되었습니다", id));
    }

    @GetMapping("/signup/store/main-category")
    public ResponseEntity<CustomResponse<?>> mainCategory() {
        List<SignUpCategoryDto> allMainCategory = categoryService.getAllMainCategory();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomResponse.ok("모든 대분류를 불러왔습니다", allMainCategory));
    }

    @GetMapping("/signup/store/sub-category")
    public ResponseEntity<CustomResponse<?>> subCategory(@RequestParam Long mainCategory) {
        List<SignUpCategoryDto> subCategories = categoryService.getSubCategories(mainCategory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomResponse.ok("대분류에 해당하는 모든 소분류를 불러왔습니다", subCategories));
    }
}
