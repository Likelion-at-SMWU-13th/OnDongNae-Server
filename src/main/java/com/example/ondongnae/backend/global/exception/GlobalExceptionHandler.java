package com.example.ondongnae.backend.global.exception;

import com.example.ondongnae.backend.global.response.CustomResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CustomResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return CustomResponse.badRequest("데이터가 누락되었습니다");
    }
}
