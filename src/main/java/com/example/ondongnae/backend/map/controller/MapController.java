package com.example.ondongnae.backend.map.controller;

import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.map.dto.StoreDataResponseDto;
import com.example.ondongnae.backend.map.dto.MapInitDataResponseDto;
import com.example.ondongnae.backend.map.service.MapService;
import com.example.ondongnae.backend.map.service.MapStoreFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;
    private final MapStoreFilterService mapStoreFilterService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getMapInitData(@CookieValue(name="language", required = false) String language){
        MapInitDataResponseDto mapInitData = mapService.getMapInitData(language);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(mapInitData));
    }

    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<?>> getMapFilter(@CookieValue(name="language", required = false) String language,
                                                       @RequestParam(required = false) Long market,
                                                       @RequestParam(required = false) Long main,
                                                       @RequestParam(required = false) List<Long> sub){
        List<StoreDataResponseDto> storeDataResponseDtoList = mapStoreFilterService.getFilteredStoreDtoList(language, market, main, sub);
        if (storeDataResponseDtoList == null || storeDataResponseDtoList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.ok("조건과 일치하는 가게가 없습니다.", null));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(storeDataResponseDtoList));
    }

}
