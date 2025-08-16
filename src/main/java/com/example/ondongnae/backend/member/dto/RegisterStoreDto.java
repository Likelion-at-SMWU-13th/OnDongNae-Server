package com.example.ondongnae.backend.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegisterStoreDto {

    @NotNull
    private String storeName;
    @NotNull
    private String address;

    private String phoneNum;
    @NotNull
    private Long mainCategory;
    @NotNull
    private List<Long> subCategory;
    @NotNull
    private String marketName;

    private List<MultipartFile> image;
    private String strength;
    private String recommendation;

    @NotNull
    private Long userId;

}
