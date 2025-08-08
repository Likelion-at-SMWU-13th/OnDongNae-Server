package com.example.ondongnae.backend.store.service;

import com.example.ondongnae.backend.category.model.MainCategory;
import com.example.ondongnae.backend.category.model.StoreSubCategory;
import com.example.ondongnae.backend.category.model.SubCategory;
import com.example.ondongnae.backend.category.repository.MainCategoryRepository;
import com.example.ondongnae.backend.category.repository.StoreSubCategoryRepository;
import com.example.ondongnae.backend.category.repository.SubCategoryRepository;
import com.example.ondongnae.backend.global.dto.LatLngResponseDto;
import com.example.ondongnae.backend.global.dto.TranslateResponseDto;
import com.example.ondongnae.backend.global.service.FileService;
import com.example.ondongnae.backend.global.service.MapService;
import com.example.ondongnae.backend.global.service.TranslateService;
import com.example.ondongnae.backend.market.model.Market;
import com.example.ondongnae.backend.market.repository.MarketRepository;
import com.example.ondongnae.backend.member.dto.RegisterStoreDto;
import com.example.ondongnae.backend.member.model.Member;
import com.example.ondongnae.backend.member.repository.MemberRepository;
import com.example.ondongnae.backend.store.dto.DescriptionCreateRequestDto;
import com.example.ondongnae.backend.store.dto.DescriptionResponseDto;
import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.model.StoreImage;
import com.example.ondongnae.backend.store.model.StoreIntro;
import com.example.ondongnae.backend.store.repository.StoreImageRepository;
import com.example.ondongnae.backend.store.repository.StoreIntroRepository;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreSubCategoryRepository storeSubCategoryRepository;
    @Value("${DESC_API_URL}")
    private String API_URL;
    
    private final MemberRepository memberRepository;
    private final MarketRepository marketRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository SubCategoryRepository;
    private final TranslateService translateService;
    private final MapService mapService;
    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreIntroRepository storeIntroRepository;
    private final FileService fileService;

    public Long registerStore(RegisterStoreDto registerStoreDto) {

        // 설명 생성
        DescriptionCreateRequestDto descriptionCreateRequestDto = createDescriptionCreateRequestDto(registerStoreDto);
        DescriptionResponseDto descriptionResponseDto = generateDescription(descriptionCreateRequestDto);
        if (descriptionResponseDto == null)
            return -2L;

        String storeName = registerStoreDto.getStoreName();
        Member member = memberRepository.findById(registerStoreDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));
        Market market = marketRepository.findByNameKo(registerStoreDto.getMarketName())
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 시장이 존재하지 않습니다."));
        MainCategory mainCategory = mainCategoryRepository.findById(registerStoreDto.getMainCategory())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 대분류가 존재하지 않습니다."));

        // 가게 이름 번역
        TranslateResponseDto translateName = translateService.translate(storeName);
        TranslateResponseDto translateAddress = translateService.translate(registerStoreDto.getAddress());

        // 위도 경도 변환
        LatLngResponseDto latLngByAddress = mapService.getLatLngByAddress(registerStoreDto.getAddress());
        if (latLngByAddress == null) {
            return -1L;
        }

        // 가게 저장
        Store store = Store.builder().member(member).market(market).mainCategory(mainCategory)
                .nameEn(translateName.getEnglish()).nameKo(registerStoreDto.getStoreName())
                .nameJa(translateName.getJapanese()).nameZh(translateName.getChinese())
                .addressKo(registerStoreDto.getAddress()).addressJa(translateAddress.getJapanese())
                .addressEn(translateAddress.getEnglish()).addressZh(translateAddress.getChinese())
                .phone(registerStoreDto.getPhoneNum()).lat(latLngByAddress.getLat()).lng(latLngByAddress.getLng()).build();

        Store savedStore = storeRepository.save(store);

        // 가게 소분류 저장
        saveStoreCategories(registerStoreDto.getSubCategory(), savedStore);

        // 설명 번역 및 저장
        saveStoreIntro(descriptionResponseDto, savedStore);

        // 가게 이미지 저장
        int order = 1;
        for (MultipartFile file : registerStoreDto.getImage()) {
            String imageUrl = fileService.uploadFile(file);
            if (imageUrl == null) {
                return -3L;
            }
            StoreImage storeImage = StoreImage.builder().store(savedStore)
                    .url(imageUrl).order(order++).build();
            storeImageRepository.save(storeImage);
        }
        return savedStore.getId();
    }

    private void saveStoreIntro(DescriptionResponseDto descriptionResponseDto, Store store) {
        String shortDescription = descriptionResponseDto.getShort_description();
        String longDescription = descriptionResponseDto.getLong_description();

        TranslateResponseDto translateShort = translateService.translate(shortDescription);
        TranslateResponseDto translateLong = translateService.translate(longDescription);

        StoreIntro en = StoreIntro.builder().store(store).lang("en").longIntro(translateLong.getEnglish()).shortIntro(translateShort.getEnglish()).build();
        StoreIntro ko = StoreIntro.builder().store(store).lang("ko").longIntro(descriptionResponseDto.getLong_description()).shortIntro(descriptionResponseDto.getShort_description()).build();
        StoreIntro zh = StoreIntro.builder().store(store).lang("zh").longIntro(translateLong.getChinese()).shortIntro(translateShort.getChinese()).build();
        StoreIntro ja = StoreIntro.builder().store(store).lang("ja").longIntro(translateLong.getJapanese()).shortIntro(translateShort.getJapanese()).build();

        storeIntroRepository.save(en);
        storeIntroRepository.save(ko);
        storeIntroRepository.save(zh);
        storeIntroRepository.save(ja);
    }

    private void saveStoreCategories(List<Long> subCategoryIds, Store savedStore) {
        for (Long id : subCategoryIds) {
            SubCategory subCategory = SubCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 id의 소분류가 존재하지 않습니다."));
            StoreSubCategory storeSubCategory = StoreSubCategory.builder().subCategory(subCategory).store(savedStore).build();
            storeSubCategoryRepository.save(storeSubCategory);
        }
    }

    private DescriptionCreateRequestDto createDescriptionCreateRequestDto(RegisterStoreDto registerStoreDto) {
        // fastAPI에 요청하기 위한 DescriptionCreateRequestDto 생성
        List<String> subCategories = new ArrayList<>();
        String recommendation = registerStoreDto.getRecommendation();
        String strength = registerStoreDto.getStrength();

        if (recommendation == null)
            registerStoreDto.setRecommendation("");
        if (strength == null)
            registerStoreDto.setStrength("");

        MainCategory mainCategory = mainCategoryRepository.findById(registerStoreDto.getMainCategory())
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 대분류가 존재하지 않습니다."));

        for (Long id : registerStoreDto.getSubCategory()) {
            SubCategory subCategory = SubCategoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 id의 소분류가 존재하지 않습니다."));
            subCategories.add(subCategory.getNameKo());
        }

        DescriptionCreateRequestDto descriptionCreateRequestDto = DescriptionCreateRequestDto.builder()
                .name(registerStoreDto.getMarketName())
                .address(registerStoreDto.getAddress())
                .mainCategory(mainCategory.getNameKo())
                .subCategory(subCategories)
                .strength(registerStoreDto.getStrength())
                .recommendation(registerStoreDto.getRecommendation()).build();
        return descriptionCreateRequestDto;
    }

    public DescriptionResponseDto generateDescription(DescriptionCreateRequestDto descriptionCreateRequestDto) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DescriptionCreateRequestDto> requestEntity = new HttpEntity<>(descriptionCreateRequestDto, headers);

        DescriptionResponseDto aiResponse;

        try {
            aiResponse = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, DescriptionResponseDto.class).getBody();
        } catch (Exception e) {
            return null;
        }
        return aiResponse;
    }
}
