package com.example.ondongnae.backend.map.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class CategoryDto {

    private Long mainCategoryId;
    private String mainCategoryName;
    private List<Map<String, Object>> subCategories;

}
