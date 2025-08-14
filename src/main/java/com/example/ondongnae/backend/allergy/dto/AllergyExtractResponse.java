package com.example.ondongnae.backend.allergy.dto;

import java.util.List;

public final class AllergyExtractResponse {
    private final List<Item> results;

    public AllergyExtractResponse(List<Item> results) {
        this.results = (results == null) ? List.of() : List.copyOf(results); // 불변화
    }

    public List<Item> getResults() { return results; }

    // 결과의 단일 행(메뉴 1개)의 스냅샷
    public static final class Item {
        private final Long menuId;
        private final String nameKo;
        private final List<String> allergiesCanonical; // 캐논명(영문) 리스트

        public Item(Long menuId, String nameKo, List<String> allergiesCanonical) {
            this.menuId = menuId;
            this.nameKo = nameKo;
            this.allergiesCanonical = (allergiesCanonical == null)
                    ? List.of() : List.copyOf(allergiesCanonical);
        }
        public Long getMenuId() { return menuId; }
        public String getNameKo() { return nameKo; }
        public List<String> getAllergiesCanonical() { return allergiesCanonical; }
    }
}
