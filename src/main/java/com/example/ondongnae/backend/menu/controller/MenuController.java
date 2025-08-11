package com.example.ondongnae.backend.menu.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.menu.dto.ManualMenuCreateRequest;
import com.example.ondongnae.backend.menu.dto.ManualMenuCreateResponse;
import com.example.ondongnae.backend.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/menus")
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/manual")
    public ResponseEntity<ApiResponse<ManualMenuCreateResponse>> createManualMenu(
            @RequestBody ManualMenuCreateRequest request
    ) {
        var response = menuService.createManual(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("수기 메뉴 등록 성공", response));
    }
}
