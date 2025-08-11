package com.example.ondongnae.backend.currency.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PriceWithRateResponseDto {

    private BigDecimal price;
    private BigDecimal exchangeRate;

}
