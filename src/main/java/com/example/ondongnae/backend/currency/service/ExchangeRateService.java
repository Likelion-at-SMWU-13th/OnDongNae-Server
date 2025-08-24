package com.example.ondongnae.backend.currency.service;

import com.example.ondongnae.backend.currency.dto.ExchangeRateDto;
import com.example.ondongnae.backend.currency.dto.ExchangeRateResponseDto;
import com.example.ondongnae.backend.currency.dto.PriceWithRateResponseDto;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExchangeRateService {

    @Value("${CURRENCY_API_KEY}")
    private String CURRENCY_API_KEY;

    public PriceWithRateResponseDto getPriceWithRate(String currency, BigDecimal price) {

        String currencyUpperCase = currency.toUpperCase();

        // 환율 API로부터 환율 정보 받아오기
        ExchangeRateResponseDto exchangeRateResponseDto = getCurrencyRateResponseDto();
        ExchangeRateDto rates = exchangeRateResponseDto.getRates();

        // 환율 변환 (기준을 EUR -> KRW로)
        BigDecimal rateKrwToOther = getRateKrwToOther(currencyUpperCase, rates);

        // 환율에 따른 가격 변환
        BigDecimal convertedPrice = rateKrwToOther.multiply(price)
                .setScale(2, RoundingMode.HALF_UP);

        // KRW 기준 환율, 변환된 가격 반환
        return PriceWithRateResponseDto.builder()
                .price(convertedPrice).exchangeRate(rateKrwToOther.setScale(6, BigDecimal.ROUND_HALF_UP)).build();
    }

    private BigDecimal getRateKrwToOther(String currencyUpperCase, ExchangeRateDto rates) {
        // KRW -> EUR
        BigDecimal krwToEur = BigDecimal.ONE.divide(rates.getKRW(), 6, BigDecimal.ROUND_HALF_UP);
        // ( KRW -> EUR ) * ( EUR -> ? ) => ( KRW -> ? )
        BigDecimal krwToOther = switch (currencyUpperCase) {
            case "USD" -> krwToEur.multiply(rates.getUSD());
            case "JPY" -> krwToEur.multiply(rates.getJPY());
            case "EUR" -> krwToEur;
            case "CNY" -> krwToEur.multiply(rates.getCNY());
            default -> throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "지원하지 않은 화폐 형식입니다.");
        };

        return krwToOther;
    }

    private ExchangeRateResponseDto getCurrencyRateResponseDto() {
        String API_URL = "http://data.fixer.io/api/latest?access_key=" + CURRENCY_API_KEY + "&symbols=USD,JPY,KRW,CNY";
        RestTemplate restTemplate = new RestTemplate();

        ExchangeRateResponseDto exchangeRateResponseDto;

        try {
            exchangeRateResponseDto = restTemplate.exchange(API_URL, HttpMethod.GET, new HttpEntity<>(null, null), ExchangeRateResponseDto.class).getBody();

            if (exchangeRateResponseDto == null)
                throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "환율 API로부터 응답을 받아오지 못했습니다.");
            if (!exchangeRateResponseDto.getSuccess())
                throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "환율 API로부터 에러가 발생했습니다." );

        } catch (ResourceAccessException e) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "환율 API 연결에 실패했습니다.");
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "환율 API에서 알 수 없는 오류가 발생했습니다.");
        }
        return exchangeRateResponseDto;
    }
}
