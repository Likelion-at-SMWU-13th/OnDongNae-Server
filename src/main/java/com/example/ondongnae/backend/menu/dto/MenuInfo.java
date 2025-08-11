package com.example.ondongnae.backend.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MenuInfo {
    private Long menuId;
    private String nameKo;
    private int priceKrw;
    private List<String> allergies;
}
