package com.example.ondongnae.backend.store.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.store.dto.BusinessHourRequest;
import com.example.ondongnae.backend.store.dto.BusinessHourResponse;
import com.example.ondongnae.backend.store.service.BusinessHourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/business-hours")
public class BusinessHourController {

    private final BusinessHourService businessHourService;

    @PutMapping
    public ResponseEntity<ApiResponse<Map<String, Integer>>> save(@Valid @RequestBody BusinessHourRequest request) {
        int saved = businessHourService.saveBusinessHour(request);
        return ResponseEntity.ok(ApiResponse.ok("영업시간 저장 완료", Map.of("saved", saved)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<BusinessHourResponse>> get() {
        BusinessHourResponse businessHourResponse = businessHourService.getBusinessHour();
        return ResponseEntity.ok(ApiResponse.ok("영업시간 조회 성공", businessHourResponse));
    }

}
