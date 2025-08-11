package com.example.ondongnae.backend.menu.service;

import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.global.service.TranslateService;
import com.example.ondongnae.backend.member.service.AuthService;
import com.example.ondongnae.backend.menu.dto.ManualMenuCreateRequest;
import com.example.ondongnae.backend.menu.dto.ManualMenuCreateResponse;
import com.example.ondongnae.backend.menu.dto.MenuInfo;
import com.example.ondongnae.backend.menu.model.Menu;
import com.example.ondongnae.backend.menu.repository.MenuRepository;
import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final AuthService authService;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final TranslateService translateService;

    // 수기 메뉴 등록
    @Transactional
    public ManualMenuCreateResponse createManual(ManualMenuCreateRequest request) {
        // 1. 토큰 -> 내 가게 ID 추출
        Long storeId = authService.getMyStoreId();

        // 2. 가게 엔티티 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND));

        // 3. 메뉴 저장
        List<Long> ids = new ArrayList<>();
        for (var item : request.getItems()) {
            var tr = translateService.translate(item.getNameKo());
            String en = nvl(tr.getEnglish(), item.getNameKo());
            String ja = nvl(tr.getJapanese(), item.getNameKo());
            String zh = nvl(tr.getChinese(), item.getNameKo());

            Menu menu = Menu.builder()
                    .store(store)
                    .nameKo(item.getNameKo())
                    .nameEn(en)
                    .nameJa(ja)
                    .nameZh(zh)
                    .priceKrw(item.getPriceKrw())
                    .build();

            menuRepository.save(menu);
            ids.add(menu.getId());
        }
        return ManualMenuCreateResponse.builder().menuIds(ids).build();
    }

    private String nvl(String v, String fb) { return (v == null || v.isBlank()) ? fb : v; }

    // 메뉴 목록 조회
    @Transactional(readOnly = true)
    public List<MenuInfo> getMenus() {
        Long storeId = authService.getMyStoreId();

        List<Menu> menus = menuRepository.findWithAllergiesByStoreId(storeId);

        return menus.stream()
                .map(m -> MenuInfo.builder()
                        .menuId(m.getId())
                        .nameKo(m.getNameKo())
                        .priceKrw(m.getPriceKrw())
                        .allergies(
                                m.getMenuAllergies().stream()
                                        .map(ma -> ma.getAllergy().getLabelKo())
                                        .filter(Objects::nonNull)
                                        .distinct()
                                        .sorted()
                                        .toList()
                        )
                        .build())
                .toList();
    }

}
