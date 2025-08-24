package com.example.ondongnae.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyProfileUpdateRequest {

    @NotBlank private String memberPhone;
    @NotBlank private String storeNameKo;
    @NotBlank private String storeAddressKo;
    private String storePhone;

    // 두 칸 모두 들어와야 변경 처리
    @Size(min = 4, max = 64)
    private String newPassword;

    @Size(min = 4, max = 64)
    private String confirmPassword;
}
