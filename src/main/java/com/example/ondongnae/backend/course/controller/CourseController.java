package com.example.ondongnae.backend.course.controller;

import com.example.ondongnae.backend.course.dto.OptionAndMarketRequestDto;
import com.example.ondongnae.backend.course.service.OptionService;
import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {

    private final OptionService optionService;
    private final MarketService marketService;

    @GetMapping("/options")
    public ResponseEntity<ApiResponse<?>> getOptions(@CookieValue(name="language", required = false) String language) {
        List<OptionAndMarketRequestDto> options = optionService.getOptionNames(language);
        List<OptionAndMarketRequestDto> markets = marketService.getMarketNames(language);
        Map<String, List<OptionAndMarketRequestDto>> result = new HashMap<>();
        result.put("market", markets);
        result.put("option", options);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok("시장과 옵션을 모두 불러왔습니다", result));
    }
}
