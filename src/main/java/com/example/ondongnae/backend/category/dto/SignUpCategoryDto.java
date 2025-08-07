package com.example.ondongnae.backend.category.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpCategoryDto {
    private String name;
    private Long id;
}
