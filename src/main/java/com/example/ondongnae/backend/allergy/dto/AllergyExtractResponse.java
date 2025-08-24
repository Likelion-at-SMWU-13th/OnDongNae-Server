package com.example.ondongnae.backend.allergy.dto;

import java.util.List;

public final class AllergyExtractResponse {
    private final List<Item> results;

    public AllergyExtractResponse(List<Item> results) {
        this.results = (results == null) ? List.of() : List.copyOf(results);
    }
    public List<Item> getResults() { return results; }

    // 결과의 단일 행(메뉴 1개)
    public static final class Item {
        private final Long menuId;
        private final String nameKo;

        // 화면 표시용(한국어)
        private final List<String> allergiesKo;

        // 저장/확정용(캐논EN)
        private final List<String> allergiesCanonical;

        public Item(Long menuId, String nameKo,
                    List<String> allergiesKo,
                    List<String> allergiesCanonical) {
            this.menuId = menuId;
            this.nameKo = nameKo;
            this.allergiesKo = (allergiesKo == null) ? List.of() : List.copyOf(allergiesKo);
            this.allergiesCanonical = (allergiesCanonical == null) ? List.of() : List.copyOf(allergiesCanonical);
        }

        public Item(Long menuId, String nameKo, List<String> allergiesCanonical) {
            this(menuId, nameKo, List.of(), allergiesCanonical);
        }

        public Long getMenuId() { return menuId; }
        public String getNameKo() { return nameKo; }
        public List<String> getAllergiesKo() { return allergiesKo; }
        public List<String> getAllergiesCanonical() { return allergiesCanonical; }
    }
}
