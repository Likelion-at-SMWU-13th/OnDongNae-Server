package com.example.ondongnae.backend.member.dto;

import lombok.Data;

@Data
public class TokenDto {
    String accessToken;
    String refreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
