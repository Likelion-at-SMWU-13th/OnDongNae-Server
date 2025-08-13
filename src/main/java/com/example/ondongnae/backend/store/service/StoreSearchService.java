package com.example.ondongnae.backend.store.service;

import com.example.ondongnae.backend.map.dto.StoreDataResponseDto;
import com.example.ondongnae.backend.map.service.MapStoreFilterService;
import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreSearchService {

    private final StoreRepository storeRepository;
    private final MapStoreFilterService mapStoreFilterService;

    public List<StoreDataResponseDto> searchStoreByKeyword(String lang, String keyword) {

        String language = lang == null ? "en" : lang.strip().toLowerCase();

        // 가게 검색
        List<Store> stores = storeRepository.findByKeyword(keyword);

        // StoreDataResponseDto 리스트 생성
        List<StoreDataResponseDto> storeDataResponseDtoList = mapStoreFilterService.getStoreDataResponseDtos(stores, language);

        return storeDataResponseDtoList;
    }
}
