package com.example.ondongnae.backend.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DescriptionCreateRequestDto {

    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private String mainCategory;
    @NotNull
    private List<String> subCategory;

    private String strength;
    private String recommendation;

}
