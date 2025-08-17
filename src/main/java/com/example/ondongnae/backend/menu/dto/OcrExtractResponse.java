package com.example.ondongnae.backend.menu.dto;

import java.util.List;

public record OcrExtractResponse(Long storeId, List<OcrExtractItemDto> items) {}
