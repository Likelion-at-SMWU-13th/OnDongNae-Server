package com.example.ondongnae.backend.member.controller;

import com.example.ondongnae.backend.category.dto.SignUpCategoryDto;
import com.example.ondongnae.backend.category.service.CategoryService;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.member.dto.MyProfileResponse;
import com.example.ondongnae.backend.member.dto.SignUpDto;
import com.example.ondongnae.backend.member.dto.RegisterStoreDto;
import com.example.ondongnae.backend.member.dto.TokenDto;
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
    public ResponseEntity<ApiResponse<?>> signupUser(@Valid @RequestBody SignUpDto signUpDto) {;
        Map<String, Object> idAndToken = authService.addUser(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("회원가입에 성공했습니다.", idAndToken));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody Map<String, String> loginDto) {
        String loginId = loginDto.get("id");
        String password = loginDto.get("password");

        if (loginId == null || password == null) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "아이디와 비밀번호를 모두 입력하세요");
        }

        TokenDto tokens = authService.login(loginId, password);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok("로그인에 성공했습니다", tokens));

    }

    @PostMapping("/signup/store")
    public ResponseEntity<ApiResponse<?>> registerStore(@Valid @ModelAttribute RegisterStoreDto registerStoreDto) {
        Long id = storeService.registerStore(registerStoreDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("가게가 등록되었습니다", id));
    }

    @GetMapping("/signup/store/main-category")
    public ResponseEntity<ApiResponse<?>> mainCategory() {
        List<SignUpCategoryDto> allMainCategory = categoryService.getAllMainCategory();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok("모든 대분류를 불러왔습니다", allMainCategory));
    }

    @GetMapping("/signup/store/sub-category")
    public ResponseEntity<ApiResponse<?>> subCategory(@RequestParam Long mainCategory) {
        List<SignUpCategoryDto> subCategories = categoryService.getSubCategories(mainCategory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok("대분류에 해당하는 모든 소분류를 불러왔습니다", subCategories));
    }


    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<?>> reissue(@RequestBody String refreshToken) {
        TokenDto newTokens = authService.reissue(refreshToken);
        return ResponseEntity.ok().body(ApiResponse.ok(newTokens));

    // 내 정보 조회
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile() {
        var data = storeService.getMyProfile();
        return ResponseEntity.ok(ApiResponse.ok("내 정보 조회 성공", data));

    }
}
