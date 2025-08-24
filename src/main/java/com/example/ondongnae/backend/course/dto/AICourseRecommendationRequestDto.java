package com.example.ondongnae.backend.course.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AICourseRecommendationRequestDto {

    private String market_name;
    private String with_option;
    private String atmosphere_option;

}
