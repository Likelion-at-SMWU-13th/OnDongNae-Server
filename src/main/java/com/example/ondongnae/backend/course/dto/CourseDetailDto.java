package com.example.ondongnae.backend.course.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseDetailDto {

    private String title;
    private String description;
    private List<RecommendedCourseStoreDto> recommendedCourseStores;

}
