package com.example.ondongnae.backend.global.response;

import com.example.ondongnae.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String code;
    private String message;

    // 성공
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, "200", "요청이 성공했습니다.");
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, "200", message);
    }

    // 실패
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null, errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String customMessage) {
        return new ApiResponse<>(false, null, errorCode.getCode(), customMessage);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, T data) {
        return new ApiResponse<>(false, data, errorCode.getCode(), errorCode.getMessage());
    }
}
