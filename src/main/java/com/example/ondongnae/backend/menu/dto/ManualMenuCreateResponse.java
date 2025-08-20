package com.example.ondongnae.backend.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ManualMenuCreateResponse {
    private List<MenuDto> menus;
    private boolean canExtractAllergy;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MenuDto {
        private String nameKo;
        private int priceKrw;
    }
}
