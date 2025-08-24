package com.example.ondongnae.backend.currency.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExchangeRateResponseDto {

    private Boolean success;
    private Long timestamp;
    private String base;
    private LocalDate date;
    private ExchangeRateDto rates;

}
