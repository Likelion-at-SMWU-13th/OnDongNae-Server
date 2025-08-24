package com.example.ondongnae.backend.global.service;

import com.example.ondongnae.backend.global.dto.TranslateResponseDto;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslateService {

    @Value("${TRANSLATE_API_URL}")
    private String TRANSLATE_API_URL;

    public TranslateResponseDto translate(String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("sentence", message);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            TranslateResponseDto translateResponseDto = restTemplate.exchange(TRANSLATE_API_URL, HttpMethod.POST, requestEntity, TranslateResponseDto.class).getBody();

            if (translateResponseDto == null)
                throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API로부터 응답을 받아오지 못했습니다.");

            return translateResponseDto;
        } catch (ResourceAccessException e) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API 연결에 실패했습니다.");
        } catch (Exception e) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API 호출 중 알 수 없는 오류가 발생했습니다.");
        }
    }
}
