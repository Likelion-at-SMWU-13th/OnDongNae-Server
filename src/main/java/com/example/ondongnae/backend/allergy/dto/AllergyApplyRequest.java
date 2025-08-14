package com.example.ondongnae.backend.allergy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public final class AllergyApplyRequest {
    // menuId -> [canonical EN ...]
    private final Map<Long, List<String>> menuAllergies;

    @JsonCreator
    public AllergyApplyRequest(@JsonProperty("menuAllergies") Map<Long, List<String>> menuAllergies) {
        this.menuAllergies = (menuAllergies == null) ? Map.of() : Map.copyOf(menuAllergies);
    }

    public Map<Long, List<String>> getMenuAllergies() {
        return menuAllergies;
    }
}
