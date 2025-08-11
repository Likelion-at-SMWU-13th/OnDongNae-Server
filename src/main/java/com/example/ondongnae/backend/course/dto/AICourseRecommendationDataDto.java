package com.example.ondongnae.backend.course.dto;

import lombok.Data;

import java.util.List;

@Data
public class AICourseRecommendationDataDto {

    private String course_title;
    private String course_long_description;
    private String course_short_description;
    private List<AIRecommendedCourseStoreDto> course_store;

}
