package com.example.ondongnae.backend.store.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.store.dto.StoreDescriptionDto;
import com.example.ondongnae.backend.store.dto.StoreDetailResponse;
import com.example.ondongnae.backend.store.service.StoreDescriptionService;
import com.example.ondongnae.backend.store.service.StoreDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreDetailService storeDetailService;
    private final StoreDescriptionService storeDescriptionService;

    // 가게 상세 정보 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreDetailResponse>> getStoreDetail(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "en") String lang
    ) {
        var data = storeDetailService.getDetail(storeId, lang);
        return ResponseEntity.ok(ApiResponse.ok("가게 상세 조회 성공", data));
    }

    // 가게 설명 조회
    @GetMapping("/me/store/description")
    public ResponseEntity<ApiResponse<?>> getStoreDetailDescription(@RequestParam String ver) {
        Object storeDescription = storeDescriptionService.getStoreDescription(ver);
        return ResponseEntity.ok(ApiResponse.ok(storeDescription));
    }

}
