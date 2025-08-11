package com.example.ondongnae.backend.course.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionAndMarketRequestDto {

    private Long id;
    private String name;

}
