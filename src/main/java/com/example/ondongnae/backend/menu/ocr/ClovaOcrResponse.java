package com.example.ondongnae.backend.menu.ocr;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// CLOVA OCR 응답 JSON을 역직렬화
@Getter
@NoArgsConstructor
public class ClovaOcrResponse {
    private String version;
    private String requestId;
    private long timestamp;
    private List<ImageResult> images;

    @Getter @NoArgsConstructor
    public static class ImageResult {
        private String uid;
        private String name;
        private String inferResult;       // SUCCESS / FAILURE
        private String message;
        private List<Field> fields;
    }
    @Getter @NoArgsConstructor
    public static class Field {
        private String inferText;         // 인식 텍스트
        private Double inferConfidence;   // 신뢰도(옵션)
        private Boolean lineBreak;        // 줄바꿈 여부(옵션)
    }
}
