package com.example.ondongnae.backend.course.service;

import com.example.ondongnae.backend.course.dto.*;
import com.example.ondongnae.backend.course.model.Course;
import com.example.ondongnae.backend.course.model.CourseStore;
import com.example.ondongnae.backend.course.model.Option;
import com.example.ondongnae.backend.course.repository.CourseRepository;
import com.example.ondongnae.backend.course.repository.CourseStoreRepository;
import com.example.ondongnae.backend.course.repository.OptionRepository;
import com.example.ondongnae.backend.global.dto.TranslateResponseDto;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.global.service.LanguageService;
import com.example.ondongnae.backend.global.service.TranslateService;
import com.example.ondongnae.backend.market.model.Market;
import com.example.ondongnae.backend.market.repository.MarketRepository;
import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.model.StoreIntro;
import com.example.ondongnae.backend.store.repository.StoreIntroRepository;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    @Value("${COURSE_RECOMMENDATION_API_URL}")
    private String RECOMMENDATION_API_URL;

    private final StoreIntroRepository storeIntroRepository;
    private final CourseStoreRepository courseStoreRepository;
    private final StoreRepository storeRepository;
    private final OptionRepository optionRepository;
    private final MarketRepository marketRepository;
    private final CourseRepository courseRepository;
    private final TranslateService translateService;
    private final LanguageService languageService;

    public CourseRecommendResponseDto getCourseRecommendationByAI(SelectedOptionDto selectedOptionDto, String lang) {

        // 코스 생성
        AICourseRecommendationResponseDto aiCourseRecommendationResponseDto = getCourseRecommendationResponseDto(selectedOptionDto);

        // 코스 번역 및 저장
        Course course = translateAndSaveRecommendation(aiCourseRecommendationResponseDto);

        // 언어에 따른 코스 추천 반환 -------
        List<CourseStore> courseStores = courseStoreRepository.findByCourseId(course.getId());
        String language = lang == null ? "en" : lang.strip().toLowerCase();

        String title = languageService.pickByLang(course.getTitleEn(), course.getTitleJa(), course.getTitleZh(), language);
        String longDescription = languageService.pickByLang(course.getLongDescriptionEn(), course.getLongDescriptionJa(), course.getLongDescriptionZh(), language);
        List<RecommendedCourseStoreDto> recommendedCourseStoreDtoList = getRecommendedCourseStoreDtoList(courseStores, language);

        CourseRecommendResponseDto courseRecommendResponseDto = CourseRecommendResponseDto.builder()
                .recommendedCourseStores(recommendedCourseStoreDtoList)
                .title(title)
                .description(longDescription)
                .id(course.getId())
                .build();

        return courseRecommendResponseDto;
    }

    // 언어에 따른 코스 내 가게 정보 가져오기
    private List<RecommendedCourseStoreDto> getRecommendedCourseStoreDtoList(List<CourseStore> courseStores, String language) {
        List<RecommendedCourseStoreDto> recommendedCourseStoreDtoList = new ArrayList<>();

        for (CourseStore s : courseStores) {
            Store store = s.getStore();

            String storeName = languageService.pickByLang(store.getNameEn(), store.getNameJa(), store.getNameZh(), language);

            long order = s.getOrder();

            StoreIntro storeIntro = storeIntroRepository.findFirstByStoreIdAndLang(store.getId(), language).orElse(null);
            String longIntro = storeIntro.getLongIntro();
            String shortIntro = storeIntro.getShortIntro();


            RecommendedCourseStoreDto dto = RecommendedCourseStoreDto.builder().name(storeName)
                    .longDescription(longIntro).shortDescription(shortIntro).order(order).build();
            recommendedCourseStoreDtoList.add(dto);
        }

        return recommendedCourseStoreDtoList;
    }

    // 코스 생성
    private AICourseRecommendationResponseDto getCourseRecommendationResponseDto(SelectedOptionDto selectedOptionDto) {
        Market market = marketRepository.findById(selectedOptionDto.getMarketId())
                .orElseThrow(() -> new BaseException(ErrorCode.MARKET_NOT_FOUND, "해당 id의 시장이 존재하지 않습니다"));
        Option atmosphere = optionRepository.findById(selectedOptionDto.getAtmosphereOptionId())
                .orElseThrow(() -> new BaseException(ErrorCode.OPTION_NOT_FOUND, "해당 id의 옵션이 존재하지 않습니다."));
        Option with = optionRepository.findById(selectedOptionDto.getWithOptionId())
                .orElseThrow(() -> new BaseException(ErrorCode.OPTION_NOT_FOUND, "해당 id의 옵션이 존재하지 않습니다."));

        AICourseRecommendationRequestDto aiCourseRecommendationRequestDto = AICourseRecommendationRequestDto.builder()
                .market_name(market.getNameKo())
                .with_option(with.getNameKo())
                .atmosphere_option(atmosphere.getNameKo())
                .build();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AICourseRecommendationRequestDto> requestEntity = new HttpEntity<>(aiCourseRecommendationRequestDto, headers);

        AICourseRecommendationResponseDto aiCourseRecommendationResponseDto;

        try {
            aiCourseRecommendationResponseDto = restTemplate.exchange(RECOMMENDATION_API_URL, HttpMethod.POST, requestEntity, AICourseRecommendationResponseDto.class).getBody();

            if (aiCourseRecommendationResponseDto == null)
                throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API로부터 응답을 받아오지 못했습니다.");
            if (!aiCourseRecommendationResponseDto.getSuccess())
                throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API 에러 - " + aiCourseRecommendationResponseDto.getError());

        } catch (ResourceAccessException e) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API 연결에 실패했습니다.");
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "외부 API에서 알 수 없는 오류가 발생했습니다.");
        }
        return aiCourseRecommendationResponseDto;
    }

    // 코스 번역 및 저장
    private Course translateAndSaveRecommendation(AICourseRecommendationResponseDto aiCourseRecommendationResponseDto) {

        AICourseRecommendationDataDto data = aiCourseRecommendationResponseDto.getData();
        List<AIRecommendedCourseStoreDto> courseStores = data.getCourse_store();

        TranslateResponseDto shortDescription = translateService.translate(data.getCourse_short_description());
        TranslateResponseDto longDescription = translateService.translate(data.getCourse_long_description());
        TranslateResponseDto title = translateService.translate(data.getCourse_title());

        Course course = Course.builder().longDescriptionKo(data.getCourse_long_description())
                .longDescriptionEn(longDescription.getEnglish())
                .longDescriptionJa(longDescription.getJapanese())
                .longDescriptionZh(longDescription.getChinese())
                .shortDescriptionKo(data.getCourse_short_description())
                .shortDescriptionEn(shortDescription.getEnglish())
                .shortDescriptionZh(shortDescription.getChinese())
                .shortDescriptionJa(shortDescription.getJapanese())
                .titleKo(data.getCourse_title())
                .titleEn(title.getEnglish())
                .titleJa(title.getJapanese())
                .titleZh(title.getChinese())
                .build();

        Course savedCourse = courseRepository.save(course);

        for (AIRecommendedCourseStoreDto s : courseStores) {
            Store store = storeRepository.findById((long) s.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND, "해당 id의 가게를 찾을 수 없습니다."));
            CourseStore courseStore = CourseStore.builder().store(store).course(savedCourse).order(s.getOrder()).build();
            courseStoreRepository.save(courseStore);
        }

        return savedCourse;
    }

}
