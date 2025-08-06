package com.example.ondongnae.backend.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomResponse<T> {

    private HttpStatus httpStatus;
    private String message;
    private boolean success;
    private T data;

    // 데이터 X
    public CustomResponse(HttpStatus httpStatus, String message, boolean success) {
        this(httpStatus, message, success, null);
    }

    // 데이터 O
    public CustomResponse(HttpStatus httpStatus, String message, boolean success, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static <T> CustomResponse<T>  of(HttpStatus httpStatus, String message, boolean success, T data) {
        return new CustomResponse<T>(httpStatus, message, success, data);
    }

    // 200 OK
    public static <T> CustomResponse<T>  ok(String message, T data) {
        return new CustomResponse<T>(HttpStatus.OK, message, true, data);
    }

    // 201 CREATED
    public static <T> CustomResponse<T>  created(String message, T data) {
        return new CustomResponse<T>(HttpStatus.CREATED, message, true, data);
    }

    // 400 BAD_REQUEST
    public static <T> CustomResponse<T>  badRequest(String message) {
        return new CustomResponse<T>(HttpStatus.BAD_REQUEST, message, false);
    }

    // 401 UNAUTHORIZED
    public static <T> CustomResponse<T>  unauthorized(String message) {
        return new CustomResponse<T>(HttpStatus.UNAUTHORIZED, message, false);
    }

    // 403 FORBIDDEN
    public static <T> CustomResponse<T>  forbidden() {
        return new CustomResponse<T>(HttpStatus.FORBIDDEN, "권한이 없습니다.", false);
    }

    // 500 INTERNAL_SERVER_ERROR
    public static <T> CustomResponse<T>  serverError(String message) {
        return new CustomResponse<T>(HttpStatus.INTERNAL_SERVER_ERROR, message, false);
    }


}
