package com.example.ondongnae.backend.store.service;

import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.member.service.AuthService;
import com.example.ondongnae.backend.store.dto.StoreDescriptionDto;
import com.example.ondongnae.backend.store.model.StoreIntro;
import com.example.ondongnae.backend.store.repository.StoreIntroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreDescriptionService {

    private final AuthService authService;
    private final StoreDetailService storeDetailService;
    private final StoreIntroRepository storeIntroRepository;

    public Object getStoreDescription(String ver) {
        Long myStoreId = authService.getMyStoreId();

        Map<String, String> storeDescription = storeDetailService.getStoreDescription(myStoreId, "ko");

        if (ver.equals("short")) {
            return storeDescription.get("shortIntro");
        } else if (ver.equals("long")) {
            return storeDescription.get("longIntro");
        } else if (ver.equals("both")) {
            return StoreDescriptionDto.builder().shortDescription(storeDescription.get("shortIntro")).longDescription(storeDescription.get("longIntro")).build();
        } else {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "해당 버전의 설명이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void updateStoreDescription(String ver, String description) {
        Long myStoreId = authService.getMyStoreId();

        StoreIntro storeIntro = storeIntroRepository.findFirstByStoreIdAndLang(myStoreId, "ko").orElse(null);

        if (ver.equals("short")) {
            storeIntro.updateShortIntro(description);
        } else if (ver.equals("long")) {
            storeIntro.updateLongIntro(description);
        } else {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "해당 버전의 설명을 수정할 수 없습니다.");
        }
    }
}
