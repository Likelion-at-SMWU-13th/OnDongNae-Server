package com.example.ondongnae.backend.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MenuUpdateResponse {
    private List<Long> createdIds;
    private List<Long> updatedIds;
    private List<Long> deletedIds;
}
