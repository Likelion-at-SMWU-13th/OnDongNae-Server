package com.example.ondongnae.backend.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuUpdateRequest {
    @NotEmpty
    private List<Item> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Item {
        // 기존 메뉴는 menuId 포함, 새로 추가는 null
        private Long menuId;

        @NotBlank
        private String nameKo;

        @Positive
        private int priceKrw;
    }

}
