package com.example.ondongnae.backend.store.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.store.dto.StoreDetailResponse;
import com.example.ondongnae.backend.store.service.StoreDetailService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stores")
public class StoreController {
    private final StoreDetailService storeDetailService;

    public StoreController(StoreDetailService storeDetailService) { this.storeDetailService = storeDetailService; }

    // 가게 상세 정보 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreDetailResponse>> getStoreDetail(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "en") String lang
    ) {
        var data = storeDetailService.getDetail(storeId, lang);
        return ResponseEntity.ok(ApiResponse.ok("가게 상세 조회 성공", data));
    }
}
