package com.example.ondongnae.backend.course.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecommendedCourseStoreDto {

    private String name;
    private String longDescription;
    private int order;

}
