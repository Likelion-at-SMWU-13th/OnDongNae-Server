package com.example.ondongnae.backend.menu.service;

import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.example.ondongnae.backend.member.service.AuthService;
import com.example.ondongnae.backend.menu.dto.OcrExtractResponse;
import com.example.ondongnae.backend.menu.ocr.ClovaOcrClient;
import com.example.ondongnae.backend.menu.ocr.ClovaOcrResponse;
import com.example.ondongnae.backend.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuExtractionService {

    private final AuthService authService;
    private final StoreRepository storeRepository;
    private final ClovaOcrClient clovaOcrClient;
    private final MenuTextParser parser;

    public OcrExtractResponse extract(MultipartFile image) {
        // 토큰 -> 내 가게 ID 확인
        Long storeId = authService.getMyStoreId();

        storeRepository.findById(storeId)
                .orElseThrow(() -> new BaseException(ErrorCode.STORE_NOT_FOUND));

        if (image == null || image.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미지 파일이 비었습니다.");
        }

        var resp = clovaOcrClient.callOcr(image);
        var text = joinInferTexts(resp);

        var items = parser.parse(text);
        if (items.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "메뉴 텍스트를 찾지 못했습니다.");
        }
        return new OcrExtractResponse(storeId, items);
    }

    // fields[*].inferText를 줄바꿈 기준으로 조합
    private String joinInferTexts(ClovaOcrResponse resp) {
        if (resp.getImages() == null || resp.getImages().isEmpty()) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "CLOVA 응답에 images가 없습니다.");
        }
        var img = resp.getImages().get(0);
        if (!"SUCCESS".equalsIgnoreCase(img.getInferResult())) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR, "CLOVA 인식 실패: " + img.getMessage());
        }
        if (img.getFields() == null || img.getFields().isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (var f : img.getFields()) {
            if (f.getInferText() == null) continue;
            sb.append(f.getInferText());
            // lineBreak가 true면 줄바꿈, 아니면 공백
            if (Boolean.TRUE.equals(f.getLineBreak())) sb.append('\n');
            else sb.append(' ');
        }
        return sb.toString();
    }
}
