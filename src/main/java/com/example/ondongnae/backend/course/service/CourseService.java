package com.example.ondongnae.backend.course.service;

import com.example.ondongnae.backend.course.dto.CourseDetailDto;
import com.example.ondongnae.backend.course.dto.RecommendedCourseStoreDto;
import com.example.ondongnae.backend.course.model.Course;
import com.example.ondongnae.backend.course.model.CourseStore;
import com.example.ondongnae.backend.course.repository.CourseRepository;
import com.example.ondongnae.backend.course.repository.CourseStoreRepository;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.global.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseStoreRepository courseStoreRepository;
    private final CourseRecommendationService courseRecommendationService;
    private final LanguageService languageService;

    public CourseDetailDto getCourseDetail(Long courseId, String lang) {
        // 코스 조회
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BaseException(ErrorCode.COURSE_NOT_FOUND, "해당 id의 코스가 존재하지 않습니다."));

        // 코스 내 가게 조회
        List<CourseStore> courseStores = courseStoreRepository.findByCourseId(course.getId());

        // 언어에 따라 반환
        String language = lang == null ? "en" : lang.strip().toLowerCase();

        String title = languageService.pickByLang(course.getTitleEn(), course.getTitleJa(), course.getTitleZh(), language);
        String longDescription = languageService.pickByLang(course.getLongDescriptionEn(), course.getLongDescriptionJa(), course.getLongDescriptionZh(), language);
        List<RecommendedCourseStoreDto> recommendedCourseStoreDtoList = courseRecommendationService.getRecommendedCourseStoreDtoList(courseStores, language);

        CourseDetailDto courseDetailDto = CourseDetailDto.builder()
                .recommendedCourseStores(recommendedCourseStoreDtoList)
                .title(title)
                .description(longDescription)
                .build();

        return courseDetailDto;
    }

}
