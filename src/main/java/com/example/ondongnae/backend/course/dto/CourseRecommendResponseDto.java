package com.example.ondongnae.backend.course.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseRecommendResponseDto {

    private String storeName;
    private String longDescription;

}
