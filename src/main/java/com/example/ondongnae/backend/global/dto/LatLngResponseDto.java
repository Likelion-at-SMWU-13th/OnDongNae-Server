package com.example.ondongnae.backend.global.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatLngResponseDto {
    private Double lat;
    private Double lng;
}
