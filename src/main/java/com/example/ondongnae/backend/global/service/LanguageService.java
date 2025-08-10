package com.example.ondongnae.backend.global.service;

import org.springframework.stereotype.Service;

@Service
public class LanguageService {

    // 언어 코드에 맞는 문자열 선택 - 한글 포함
    public String pickByLang(String ko, String en, String ja, String zh, String lang) {
        return switch (lang) {
            case "ko" -> ko;
            case "ja" -> ja;
            case "zh" -> zh;
            default -> en;
        };
    }

    // 언어 코드에 맞는 문자열 선택 - 한글 포함 X
    public String pickByLang(String en, String ja, String zh, String lang) {
        return switch (lang) {
            case "ja" -> ja;
            case "zh" -> zh;
            default -> en;
        };
    }

}
