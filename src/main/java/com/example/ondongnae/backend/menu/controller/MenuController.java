package com.example.ondongnae.backend.menu.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.menu.dto.ManualMenuCreateRequest;
import com.example.ondongnae.backend.menu.dto.ManualMenuCreateResponse;
import com.example.ondongnae.backend.menu.dto.MenuInfo;
import com.example.ondongnae.backend.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/menus")
public class MenuController {
    private final MenuService menuService;

    // 수기 메뉴 등록
    @PostMapping("/manual")
    public ResponseEntity<ApiResponse<ManualMenuCreateResponse>> createManualMenu(
            @RequestBody ManualMenuCreateRequest request
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

}
