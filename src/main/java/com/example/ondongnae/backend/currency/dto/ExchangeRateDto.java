package com.example.ondongnae.backend.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateDto {

    @JsonProperty("JPY")
    private BigDecimal JPY;

    @JsonProperty("KRW")
    private BigDecimal KRW;

    @JsonProperty("USD")
    private BigDecimal USD;

    @JsonProperty("CNY")
    private BigDecimal CNY;

}
