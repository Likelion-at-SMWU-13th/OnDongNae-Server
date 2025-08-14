package com.example.ondongnae.backend.allergy.service;

import com.example.ondongnae.backend.allergy.cononical.CanonicalAllergy;
import com.example.ondongnae.backend.allergy.dto.AllergyExtractResponse;
import com.example.ondongnae.backend.allergy.gpt.AllergyGptClient;
import com.example.ondongnae.backend.allergy.heuristic.HeuristicAllergyEngine;
import com.example.ondongnae.backend.allergy.util.MenuNamePreprocessor;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.member.service.AuthService;
import com.example.ondongnae.backend.menu.model.Menu;
import com.example.ondongnae.backend.menu.repository.MenuRepository;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AllergyService {

    private final AuthService authService;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final AllergyGptClient gptClient;

    // 알레르기 추출 실행
    // 내 가게 모든 메뉴 전처리 -> GPT 추출 -> 응답 DTO 조립
    @Transactional(readOnly = true)
    public AllergyExtractResponse extractAllFromMyMenus() {
        Long storeId = authService.getMyStoreId();
        storeRepository.findById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND));

        List<Menu> menus = menuRepository.findByStoreId(storeId);
        if (menus.isEmpty()) {
            return new AllergyExtractResponse(List.of());
        }

        // 1) 휴리스틱 분석 + GPT 입력 구성
        Map<Long, HeuristicAllergyEngine.Result> heuristics = new HashMap<>();
        var inputs = new ArrayList<Map<String,Object>>();

        for (Menu m : menus) {
            var h = HeuristicAllergyEngine.analyze(m.getNameKo(), m.getNameEn(), MenuNamePreprocessor.clean(m.getNameKo()));
            heuristics.put(m.getId(), h);

            // 힌트(태그) 문자열화
            List<String> hintList = h.tags().stream().map(Enum::name).toList();

            inputs.add(Map.of(
                    "menuId", m.getId(),
                    "nameKo", m.getNameKo(),
                    "nameEn", nvl(m.getNameEn(), m.getNameKo()),
                    "cleanKo", MenuNamePreprocessor.clean(m.getNameKo()),
                    "hints", hintList
            ));
        }

        // 2) GPT 호출
        Map<Long, List<String>> canonByMenu = gptClient.extractCanonical(inputs);

        // 3) (휴리스틱 ∪ GPT) 결합
        var items = menus.stream().map(m -> {
            var h = heuristics.get(m.getId());
            Set<String> union = new LinkedHashSet<>();

            // 휴리스틱 결과 추가
            for (CanonicalAllergy c : h.allergens()) union.add(c.labelEn());
            // GPT 결과 추가
            union.addAll(canonByMenu.getOrDefault(m.getId(), List.of()));

            return new AllergyExtractResponse.Item(
                    m.getId(),
                    m.getNameKo(),
                    List.copyOf(union)
            );
        }).toList();

        return new AllergyExtractResponse(items);
    }

    private String nvl(String v, String fb) { return (v == null || v.isBlank()) ? fb : v; }
}
