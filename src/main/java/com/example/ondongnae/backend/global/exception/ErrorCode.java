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
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", "요청한 경로를 찾을 수 없습니다"),

    // 토큰 관련
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"TOKEN_EXPIRED","만료된 토큰입니다."),

    // 메뉴 관련

    // 영업 시간 관련

    // 가게 설명 관련

    // 시장 설명 관련

    // 지도 관련

    // 코스 추천 관련

    // 환율 관련

    // 최종 안전망
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String code;       // 에러 식별 코드
    private final String message;    // 기본 메시지
}