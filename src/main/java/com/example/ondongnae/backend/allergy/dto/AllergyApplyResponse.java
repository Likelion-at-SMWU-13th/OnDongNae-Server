package com.example.ondongnae.backend.allergy.dto;


import java.util.List;

public final class AllergyApplyResponse {
    private final List<Long> updatedMenuIds;

    public AllergyApplyResponse(List<Long> updatedMenuIds) {
        this.updatedMenuIds = (updatedMenuIds == null) ? List.of() : List.copyOf(updatedMenuIds);
    }
    public List<Long> getUpdatedMenuIds() { return updatedMenuIds; }
}
