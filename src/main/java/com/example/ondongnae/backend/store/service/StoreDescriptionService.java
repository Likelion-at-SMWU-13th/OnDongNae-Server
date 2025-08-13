package com.example.ondongnae.backend.store.service;

import com.example.ondongnae.backend.member.service.AuthService;
import com.example.ondongnae.backend.store.dto.StoreDescriptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreDescriptionService {

    private final AuthService authService;
    private final StoreDetailService storeDetailService;

    public StoreDescriptionDto getStoreDescription() {
        Long myStoreId = authService.getMyStoreId();

        Map<String, String> storeDescription = storeDetailService.getStoreDescription(myStoreId, "ko");

        return StoreDescriptionDto.builder().shortDescription(storeDescription.get("shortIntro")).longDescription(storeDescription.get("longIntro")).build();
    }
}
