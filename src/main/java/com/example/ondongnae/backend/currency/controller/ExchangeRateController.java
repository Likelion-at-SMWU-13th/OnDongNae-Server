package com.example.ondongnae.backend.currency.controller;

import com.example.ondongnae.backend.currency.dto.PriceWithRateResponseDto;
import com.example.ondongnae.backend.currency.service.ExchangeRateService;
import com.example.ondongnae.backend.global.response.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Validated
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/exchange")
    public ResponseEntity<ApiResponse<?>> getPriceWithExchangeRate(@NotBlank @RequestParam String currency,
                                                                   @Positive @RequestParam BigDecimal price) {
        PriceWithRateResponseDto priceWithRate = exchangeRateService.getPriceWithRate(currency, price);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.ok(priceWithRate));
    }
}
