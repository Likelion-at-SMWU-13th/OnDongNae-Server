package com.example.ondongnae.backend.course.controller;

import com.example.ondongnae.backend.course.dto.*;
import com.example.ondongnae.backend.course.service.CourseRecommendationService;
import com.example.ondongnae.backend.course.service.CourseService;
import com.example.ondongnae.backend.course.service.OptionService;
import com.example.ondongnae.backend.global.response.ApiResponse;
import com.example.ondongnae.backend.market.service.MarketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final OptionService optionService;
    private final MarketService marketService;
    private final CourseRecommendationService courseRecommendationService;
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getRandomCourses(@RequestHeader(name="Accept-Language", required = false) String language) {
        List<RandomCourseDto> randomCourses = courseService.getRandomCourses(language);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(randomCourses));
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<ApiResponse<?>> getCourseDetail(@RequestHeader(name="Accept-Language", required = false) String language,
                                                           @PathVariable Long courseId) {
        CourseDetailDto courseDetail = courseService.getCourseDetail(courseId, language);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(courseDetail));
    }

    @GetMapping("/options")
    public ResponseEntity<ApiResponse<?>> getOptions(@RequestHeader(name="Accept-Language", required = false) String language) {
        List<OptionAndMarketRequestDto> options = optionService.getOptionNames(language);
        List<OptionAndMarketRequestDto> markets = marketService.getMarketNames(language);
        Map<String, List<OptionAndMarketRequestDto>> result = new HashMap<>();
        result.put("market", markets);
        result.put("option", options);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok("시장과 옵션을 모두 불러왔습니다", result));
    }

    @PostMapping("/recommend")
    public ResponseEntity<ApiResponse<?>> courseRecommendation(@RequestHeader(name="Accept-Language", required = false) String language,
                                                           @Valid @RequestBody SelectedOptionDto selectedOptionDto) {
        CourseRecommendResponseDto courseRecommendation = courseRecommendationService.getCourseRecommendationByAI(selectedOptionDto, language);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(courseRecommendation));

    }


}
