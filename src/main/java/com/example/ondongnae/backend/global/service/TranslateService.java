package com.example.ondongnae.backend.global.service;

import com.example.ondongnae.backend.global.dto.TranslateResponseDto;
import com.example.ondongnae.backend.store.dto.DescriptionCreateRequestDto;
import com.example.ondongnae.backend.store.dto.DescriptionResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
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

        TranslateResponseDto translateResponseDto = restTemplate.exchange(TRANSLATE_API_URL, HttpMethod.POST, requestEntity, TranslateResponseDto.class).getBody();

        return translateResponseDto;
    }
}
