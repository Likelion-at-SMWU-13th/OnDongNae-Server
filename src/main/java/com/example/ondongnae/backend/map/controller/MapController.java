package com.example.ondongnae.backend.map.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.map.dto.MapInitDataResponseDto;
import com.example.ondongnae.backend.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getMapInitData(@CookieValue(name="language", required = false) String language){
        MapInitDataResponseDto mapInitData = mapService.getMapInitData(language);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(mapInitData));
    }

}
