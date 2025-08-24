package com.example.ondongnae.backend.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class SignUpDto {

    @NotNull
    private String name;
    @NotNull
    private String phoneNum;
    @NotNull
    private String loginId;
    @NotNull
    private String password1;
    @NotNull
    private String password2;

}
