package com.example.ondongnae.backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "허용되지 않은 메서드입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 경로를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),

    // 토큰 관련
    AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "토큰 인증이 필요합니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"EXPIRED_TOKEN","만료된 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_NOT_FOUND", "토큰을 찾을 수 없습니다."),

    // 외부 API 관련
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_API_ERROR", "외부 API 연결에 실패했거나 응답이 유효하지 않습니다."),
    GPT_BAD_RESPONSE(HttpStatus.BAD_GATEWAY, "GPT_BAD_RESPONSE", "모델 응답 형식이 올바르지 않습니다."),
    GPT_PROVIDER_ERROR(HttpStatus.BAD_GATEWAY, "GPT_PROVIDER_ERROR", "모델 응답 생성 중 오류가 발생했습니다."),


    // 메뉴 관련
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "MENU_NOT_FOUND", "메뉴 정보를 찾을 수 없습니다."),
    ALLERGY_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "ALLERGY_NOT_SUPPORTED", "허용 목록에 없는 알레르기 항목입니다."),

    // 가게 관련
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE_NOT_FOUND", "해당 가게를 찾을 수 없습니다."),
    BUSINESS_HOUR_NOT_FOUND(HttpStatus.NOT_FOUND, "BUSINESS_HOUR_NOT_FOUND", "영업시간 정보를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "해당 분류를 찾을 수 없습니다."),

    // 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "해당 사용자를 찾을 수 없습니다."),

    // 시장 관련
    MARKET_NOT_FOUND(HttpStatus.NOT_FOUND, "MARKET_NOT_FOUND", "해당 시장을 찾을 수 없습니다."),

    // 지도 관련

    // 코스 추천 관련
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "OPTION_NOT_FOUND", "해당 옵션을 찾을 수 없습니다."),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE_NOT_FOUND", "해당 코스를 찾을 수 없습니다."),

    // 환율 관련

    // 최종 안전망
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code;       // 에러 식별 코드
    private final String message;    // 기본 메시지
}