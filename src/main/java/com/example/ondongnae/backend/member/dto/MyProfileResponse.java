package com.example.ondongnae.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyProfileResponse {

    private String memberPhone;
    private String storeNameKo;
    private String storeAddressKo;
    private String storePhone;
}
