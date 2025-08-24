package com.example.ondongnae.backend.store.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreDescriptionDto {

    private String shortDescription;
    private String longDescription;

}
