package com.example.ondongnae.backend.map.service;

import com.example.ondongnae.backend.category.model.SubCategory;
import com.example.ondongnae.backend.category.repository.MainCategoryRepository;
import com.example.ondongnae.backend.category.repository.SubCategoryRepository;
import com.example.ondongnae.backend.global.service.LanguageService;
import com.example.ondongnae.backend.map.dto.CategoryDto;
import com.example.ondongnae.backend.map.dto.MapInitDataResponseDto;
import com.example.ondongnae.backend.map.dto.RandomStoreDto;
import com.example.ondongnae.backend.market.model.Market;
import com.example.ondongnae.backend.market.repository.MarketRepository;
import com.example.ondongnae.backend.store.dto.StoreDetailResponse;
import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.repository.BusinessHourRepository;
import com.example.ondongnae.backend.store.repository.StoreImageRepository;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import com.example.ondongnae.backend.store.service.StoreDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MarketRepository marketRepository;
    private final LanguageService languageService;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final StoreRepository storeRepository;
    private final StoreDetailService storeDetailService;
    private final BusinessHourRepository businessHourRepository;
    private final StoreImageRepository storeImageRepository;

    public MapInitDataResponseDto getMapInitData(String lang) {

        String language = lang == null ? "en" : lang.strip().toLowerCase();

        // 시장 이름 옵션
        List<Map<String, Object>> marketOptionList = getMarkets(language);

        // 대분류 옵션 + 대분류 옵션에 맞는 소분류 옵션
        List<CategoryDto> categoryDtoList = getCategoryDtos(language);

        // 가게 랜덤 추천 3개
        List<RandomStoreDto> randomStoreDtoList = getRandomStoreDtos(language);

        return MapInitDataResponseDto.builder()
                .marketOptions(marketOptionList)
                .categoryOptions(categoryDtoList)
                .randomStores(randomStoreDtoList)
                .build();
    }


    private List<Map<String, Object>> getMarkets(String language) {
        List<Market> markets = marketRepository.findAll();
        List<Map<String, Object>> marketOptionList = new ArrayList<>();

        markets.forEach(market -> {
            Map<String, Object> marketOption = new HashMap<>();
            marketOption.put("id", market.getId());
            marketOption.put("name", languageService.pickByLang(market.getNameEn(), market.getNameJa(), market.getNameZh(), language));
            marketOptionList.add(marketOption);

        });
        return marketOptionList;
    }

    private List<CategoryDto> getCategoryDtos(String language) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();

        mainCategoryRepository.findAll().forEach(m -> {
            List<Map<String, Object>> subCategoryOptionList = new ArrayList<>();

            subCategoryRepository.findByMainCategory(m).forEach(s -> {
                Map<String, Object> subCategoryOption = new HashMap<>();
                subCategoryOption.put("id", s.getId());
                subCategoryOption.put("name", languageService.pickByLang(s.getNameEn(), s.getNameJa(), s.getNameZh(), language));
                subCategoryOptionList.add(subCategoryOption);
            });

            CategoryDto categoryDto = CategoryDto.builder().mainCategoryId(m.getId())
                    .mainCategoryName(languageService.pickByLang(m.getNameEn(), m.getNameJa(), m.getNameZh(), language))
                    .subCategories(subCategoryOptionList).build();
            categoryDtoList.add(categoryDto);
        });
        return categoryDtoList;
    }

    private List<RandomStoreDto> getRandomStoreDtos(String language) {
        List<Store> stores = storeRepository.pickRandom();
        List<RandomStoreDto> randomStoreDtoList = new ArrayList<>();

        for(Store store : stores) {
            List<String> subCategories = new ArrayList<>();
            StoreDetailResponse.Status status = storeDetailService.buildTodayStatus(businessHourRepository.findByStoreId(store.getId()));

            store.getStoreSubCategories().forEach(s -> {
                SubCategory subCategory = s.getSubCategory();
                subCategories.add(languageService.pickByLang(subCategory.getNameEn(), subCategory.getNameJa(), subCategory.getNameZh(), language));
            });

            RandomStoreDto randomStoreDto = RandomStoreDto.builder()
                    .id(store.getId())
                    .name(languageService.pickByLang(store.getNameEn(), store.getNameJa(), store.getNameZh(), language))
                    .phone(store.getPhone())
                    .isOpen(status.isOpen())
                    .subCategories(subCategories)
                    .address(languageService.pickByLang(store.getAddressEn(), store.getAddressJa(), store.getAddressZh(), language))
                    .image(storeImageRepository.findFirstByStoreOrderByOrderAsc(store).getUrl())
                    .build();
            randomStoreDtoList.add(randomStoreDto);
        }
        return randomStoreDtoList;
    }
}
