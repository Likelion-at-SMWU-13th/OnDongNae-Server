package com.example.ondongnae.backend.menu.ocr;

import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

// CLOVA OCR API 호출 WebClient
@Slf4j
@Component
@RequiredArgsConstructor
public class ClovaOcrClient {

    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "pdf");

    private final WebClient clovaOcrWebClient;
    private final ClovaOcrProperties props;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // CLOVA OCR 호출 -> 응답 본문을 모델로 반환
    public ClovaOcrResponse callOcr(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미지 파일이 비어 있습니다.");
        }

        String ext = getExtLower(image.getOriginalFilename());
        if (!ALLOWED.contains(ext)) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE,
                    "지원하지 않는 이미지 형식입니다. (허용: jpg, jpeg, png, pdf)");
        }

        // 1. message JSON 생성
        String messageJson = buildMessageJson(ext);

        // ★ 디버깅용(임시): URL/버전/확장자만 찍기. secret은 찍지 않음!
        log.info("[CLOVA] url={}, version={}, ext={}, hasSecret={}",
                props.invokeUrl(), props.version(), ext, props.secretKey()!=null);

        // 2. multipart/form-data 바디 구성
        MultipartBodyBuilder mb = new MultipartBodyBuilder();
        mb.part("message", messageJson).contentType(MediaType.APPLICATION_JSON);
        mb.part("file", toByteArrayResource(image))
                .filename(Optional.ofNullable(image.getOriginalFilename()).orElse("menu.jpg"))
                .contentType(mediaTypeFor(ext)); // 실제 타입 지정

        // 3. WebClient 호출
        try {
            return clovaOcrWebClient.post()
                    .header("X-OCR-SECRET", props.secretKey())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(mb.build()))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, resp ->
                            resp.bodyToMono(String.class)
                                    .defaultIfEmpty("")
                                    .map(body -> {
                                        // ★ 에러 메시지 더 명확히
                                        String msg = "CLOVA 호출 실패: " + resp.statusCode() + " " + body;
                                        log.warn("[CLOVA][HTTP_ERROR] {}", msg);
                                        return new BaseException(ErrorCode.EXTERNAL_API_ERROR, msg);
                                    }))
                    .bodyToMono(ClovaOcrResponse.class)
                    .block(); // 동기 처리
        } catch (WebClientResponseException e) {
            // CLOVA에서 4xx/5xx와 함께 보낸 바디를 포함해서 래핑
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR,
                    "CLOVA 응답 오류: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new BaseException(ErrorCode.EXTERNAL_API_ERROR,
                    "CLOVA 호출 중 예외: " + e.getMessage());
        }
    }

    // CLOVA가 요구하는 message JSON(버전/요청ID/타임스탬프/이미지 포맷)을 생성
    private String buildMessageJson(String extLower) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("version", props.version());                       // 예: "V2"
        root.put("requestId", UUID.randomUUID().toString());
        root.put("timestamp", System.currentTimeMillis());

        ArrayNode images = root.putArray("images");
        ObjectNode img = images.addObject();
        img.put("format", extLower);                                 // 소문자 확장자
        img.put("name", "menu");

        try {
            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, "CLOVA 요청 JSON 생성 실패");
        }
    }

    private static ByteArrayResource toByteArrayResource(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            return new ByteArrayResource(bytes) {
                @Override public String getFilename() {
                    return Optional.ofNullable(file.getOriginalFilename()).orElse("menu.jpg");
                }
            };
        } catch (IOException e) {
            throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미지 읽기 실패");
        }
    }

    // 파일 확장자를 소문자로 반환
    private static String getExtLower(String filename) {
        if (filename == null) return "jpg";
        int idx = filename.lastIndexOf('.');
        String raw = (idx > -1 && idx < filename.length() - 1) ? filename.substring(idx + 1) : "jpg";
        return raw.toLowerCase(Locale.ROOT);
    }

    // 확장자에 대응하는 Content-Type
    private static MediaType mediaTypeFor(String ext) {
        return switch (ext) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png"         -> MediaType.IMAGE_PNG;
            case "pdf"         -> MediaType.APPLICATION_PDF;
            default            -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

}