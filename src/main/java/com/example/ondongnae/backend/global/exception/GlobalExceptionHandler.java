package com.example.ondongnae.backend.global.exception;

import com.example.ondongnae.backend.global.response.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(CustomResponse.badRequest("데이터가 누락되었습니다"));
    }
}
