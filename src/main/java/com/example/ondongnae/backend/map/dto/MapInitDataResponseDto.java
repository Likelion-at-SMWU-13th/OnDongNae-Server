package com.example.ondongnae.backend.map.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MapInitDataResponseDto {

    private List<Map<String, Object>> marketOptions;
    private List<CategoryDto> categoryOptions;
    private List<RandomStoreDto> randomStores;

}
