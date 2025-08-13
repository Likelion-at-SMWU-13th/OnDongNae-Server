package com.example.ondongnae.backend.map.service;

import com.example.ondongnae.backend.category.model.SubCategory;
import com.example.ondongnae.backend.category.repository.MainCategoryRepository;
import com.example.ondongnae.backend.category.repository.SubCategoryRepository;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.global.service.LanguageService;
import com.example.ondongnae.backend.map.dto.StoreDataResponseDto;
import com.example.ondongnae.backend.market.repository.MarketRepository;
import com.example.ondongnae.backend.store.dto.StoreDetailResponse;
import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.repository.BusinessHourRepository;
import com.example.ondongnae.backend.store.repository.StoreImageRepository;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import com.example.ondongnae.backend.store.service.StoreDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapStoreFilterService {

    private final StoreRepository storeRepository;
    private final MarketRepository marketRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final StoreImageRepository storeImageRepository;
    private final LanguageService languageService;
    private final StoreDetailService storeDetailService;
    private final BusinessHourRepository businessHourRepository;

    public List<StoreDataResponseDto> getFilteredStoreDtoList(String lang, Long marketId, Long mainCategoryId, List<Long> subCategoryIds) {

        String language = lang == null ? "en" : lang.strip().toLowerCase();

        if (marketId != null) {
            if (!marketRepository.existsById(marketId))
                throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "해당 id의 시장이 존재하지 않습니다.");
        }

        if (mainCategoryId != null) {
            if (!mainCategoryRepository.existsById(mainCategoryId))
                throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "해당 id의 대분류가 존재하지 않습니다.");
        }

        // 필터링된 가게 조회
        List<Store> stores;
        List<String> subCategoryNameList = new ArrayList<>();

        if (subCategoryIds != null) {
            for (Long subCategoryId : subCategoryIds) {
                SubCategory sub = subCategoryRepository.findById(subCategoryId)
                        .orElseThrow(() -> new BaseException(ErrorCode.INVALID_INPUT_VALUE, "해당 id의 소분류가 존재하지 않습니다."));

                subCategoryNameList.add(languageService.pickByLang(sub.getNameEn(), sub.getNameJa(), sub.getNameZh(), language));
            }
            List<Long> uniqueIds = subCategoryIds.stream().distinct().collect(Collectors.toList());
            stores = storeRepository.findByMarketIdAndMainCategoryIdAndSubCategoryIds(marketId, mainCategoryId, uniqueIds, Long.valueOf(uniqueIds.size()));
        } else {
            stores = storeRepository.findByMarketIdAndMainCategoryId(marketId, mainCategoryId);
        }

        // FilteredStoreDto 리스트 생성
        List<StoreDataResponseDto> storeDataResponseDtoList = new ArrayList<>();

        if (stores == null || stores.size() == 0)
            return null;
        else {
            stores.forEach(s -> {
                StoreDetailResponse.Status status = storeDetailService.buildTodayStatus(businessHourRepository.findByStoreId(s.getId()));

                StoreDataResponseDto storeDataResponseDto = StoreDataResponseDto.builder().id(s.getId())
                        .name(languageService.pickByLang(s.getNameEn(), s.getNameJa(), s.getNameZh(), language))
                        .image(storeImageRepository.findFirstByStoreOrderByOrderAsc(s).getUrl())
                        .phone(s.getPhone())
                        .address(languageService.pickByLang(s.getAddressEn(), s.getAddressJa(), s.getAddressZh(), language))
                        .subCategories(subCategoryNameList)
                        .isOpen(status.isOpen())
                        .latitude(s.getLat())
                        .longitude(s.getLng())
                        .build();

                storeDataResponseDtoList.add(storeDataResponseDto);
            });
            return storeDataResponseDtoList;
        }
    }
}
