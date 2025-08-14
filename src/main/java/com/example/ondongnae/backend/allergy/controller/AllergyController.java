package com.example.ondongnae.backend.allergy.controller;

import com.example.ondongnae.backend.allergy.dto.AllergyExtractResponse;
import com.example.ondongnae.backend.allergy.service.AllergyService;
import com.example.ondongnae.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AllergyController {

    private final AllergyService allergyService;

    // 알레르기 추출(미리보기) — 본문 없이 호출
    @PostMapping("/me/menus/allergens/extract")
    public ResponseEntity<ApiResponse<AllergyExtractResponse>> extract() {
        var result = allergyService.extractAllFromMyMenus();
        return ResponseEntity.ok(ApiResponse.ok("알레르기 추출 성공", result));
    }
}
