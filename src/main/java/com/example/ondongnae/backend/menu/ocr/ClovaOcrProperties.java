package com.example.ondongnae.backend.menu.ocr;

import org.springframework.boot.context.properties.ConfigurationProperties;

// application.properties의 clova.ocr 값을 바인딩
@ConfigurationProperties(prefix = "clova.ocr")
public record ClovaOcrProperties(
        String invokeUrl,
        String secretKey,
        String version,
        Integer connectTimeoutMs,
        Integer readTimeoutMs
) {}
