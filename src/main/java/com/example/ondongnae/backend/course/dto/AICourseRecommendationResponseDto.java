package com.example.ondongnae.backend.course.dto;

import lombok.Data;

@Data
public class AICourseRecommendationResponseDto {

    private Boolean success;
    private AICourseRecommendationDataDto data;
    private String error;

}
