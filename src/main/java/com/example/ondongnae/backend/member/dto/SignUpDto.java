package com.example.ondongnae.backend.member.dto;

import lombok.Data;

@Data
public class SignUpDto {

    private String name;
    private String phoneNum;
    private String loginId;
    private String password1;
    private String password2;

}
