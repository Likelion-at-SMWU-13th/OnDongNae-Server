package com.example.ondongnae.backend.menu.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.menu.dto.*;
import com.example.ondongnae.backend.menu.service.MenuExtractionService;
import com.example.ondongnae.backend.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/menus")
public class MenuController {
    private final MenuService menuService;
    private final MenuExtractionService menuExtractionService;

    // 메뉴 저장 (수기/OCR 공통)
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ManualMenuCreateResponse>> createManualMenu(
            @Valid @RequestBody ManualMenuCreateRequest request
    ) {
        var response = menuService.createManual(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("수기 메뉴 등록 성공", response));
    }

    // 메뉴 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuInfo>>> getAllMenus() {
        List<MenuInfo> list = menuService.getMenus();
        return ResponseEntity.ok(ApiResponse.ok("메뉴 목록 조회 성공", list));
    }

    // 메뉴 수정
    @PutMapping
    public ResponseEntity<ApiResponse<MenuUpdateResponse>> replaceAll(
            @Valid @RequestBody MenuUpdateRequest request
    ) {
        MenuUpdateResponse data = menuService.replaceAll(request);
        return ResponseEntity.ok(ApiResponse.ok("메뉴 수정 완료", data));
    }

    // OCR 메뉴 추출
    @PostMapping(value = "/allergens/extract", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<OcrExtractResponse>> extract(@RequestPart("image") MultipartFile image) {
        var res = menuExtractionService.extract(image);
        return ResponseEntity.ok(ApiResponse.ok("메뉴 추출 성공", res));
    }

}
