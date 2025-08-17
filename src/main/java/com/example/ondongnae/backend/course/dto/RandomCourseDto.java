package com.example.ondongnae.backend.course.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RandomCourseDto {

    private Long id;
    private String courseTitle;
    private String courseDescription;
    private List<String> storeNames;
    private String mainImageUrl;

}
