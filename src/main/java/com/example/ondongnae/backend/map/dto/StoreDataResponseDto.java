package com.example.ondongnae.backend.map.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StoreDataResponseDto {

    private Long id;
    private String name;
    private Boolean isOpen;
    private List<String> subCategories;
    private String address;
    private String phone;
    private String image;
    private Double latitude;
    private Double longitude;

}
