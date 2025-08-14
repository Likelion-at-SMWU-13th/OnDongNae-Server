package com.example.ondongnae.backend.allergy.gpt;

import com.example.ondongnae.backend.allergy.cononical.CanonicalAllergy;
import com.example.ondongnae.backend.global.exception.BaseException;
import com.example.ondongnae.backend.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;


/**
 * OpenAI Chat Completions 호출 래퍼
 * 허용 캐논명(영문) 화이트리스트를 system 프롬프트에 명시
 * response_format=json_object 로 JSON만 출력하도록 강제
 * 사후 검증: 허용 목록 밖 값은 버림
 */
@Component
@RequiredArgsConstructor
public class AllergyGptClient {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    private final ObjectMapper om = new ObjectMapper();

    // 허용 캐논명(영문)
    private static final List<String> ALLOWED_CANON = CanonicalAllergy.allEnglishLabels();

    /**
     * inputs: [{menuId, nameKo, nameEn, cleanKo}, ...]
     * return: menuId -> [canonical(English), ...]
     */
    public Map<Long, List<String>> extractCanonical(List<Map<String, Object>> inputs) {
        try {
            // 1. 프롬프트 구성
            String system = """
                You are an expert at identifying food allergens in Korean market menu items.
                Choose allergens ONLY from the allowed canonical list (English):
                %s
                If uncertain, return an empty array [].
                Use provided 'hints' (e.g., NOODLE, FRIED_OR_BATTER) to better infer typical hidden ingredients.
                Respond STRICTLY in JSON with this schema:
                {"results":[{"menuId":<long>,"allergiesCanonical":[<string>, ...]}, ...]}
                """.formatted(ALLOWED_CANON);

            String user = buildUserPrompt(inputs);

            // 2. OpenAI 호출
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(Duration.ofSeconds(40))
                    .build();

            String payload = """
            {
              "model": "%s",
              "temperature": 0.0,
              "response_format": {"type":"json_object"},
              "messages": [
                {"role":"system","content": %s},
                {"role":"user","content": %s}
              ]
            }
            """.formatted(model, toJson(system), toJson(user));

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .header("Authorization", "Bearer " + openaiApiKey)
                    .header("Content-Type", "application/json")
                    .post(RequestBody.create(payload, MediaType.parse("application/json")))
                    .build();

            // 3. 응답 파싱 + 사후 검증
            try (Response resp = client.newCall(request).execute()) {
                if (!resp.isSuccessful()) {
                    // OpenAI HTTP 에러 -> 게이트웨이 오류로 승격
                    throw new BaseException(ErrorCode.GPT_PROVIDER_ERROR, "HTTP " + resp.code());
                }
                String body = Objects.requireNonNull(resp.body()).string();
                String content = om.readTree(body)
                        .path("choices").get(0)
                        .path("message").path("content").asText();

                JsonNode json = om.readTree(content);
                Map<Long, List<String>> out = new HashMap<>();

                for (JsonNode item : json.path("results")) {
                    long id = item.path("menuId").asLong();
                    List<String> arr = new ArrayList<>();
                    for (JsonNode a : item.path("allergiesCanonical")) {
                        String canon = a.asText().trim();
                        if (ALLOWED_CANON.contains(canon)) arr.add(canon); // 허용 캐논만 통과
                    }
                    out.put(id, Collections.unmodifiableList(arr));
                }
                return out;
            }
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            // 모델이 JSON 스키마를 지키지 못한 경우 등
            throw new BaseException(ErrorCode.GPT_BAD_RESPONSE, e.getMessage());
        }
    }

    // few-shot + 실제 입력을 결합한 user 메시지 구축
    private String buildUserPrompt(List<Map<String, Object>> inputs) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
        [few-shot]
        Input:
        - menuId: 1, ko: "떡볶이", en: "Tteokbokki", cleanKo: "떡볶이", hints: [NOODLE]
        Output:
        {"results":[{"menuId":1,"allergiesCanonical":["Wheat (Gluten)"]}]}
    
        Input:
        - menuId: 2, ko: "오징어튀김(대)", en: "Fried squid (L)", cleanKo: "오징어튀김", hints: [FRIED_OR_BATTER, SQUID]
        Output:
        {"results":[{"menuId":2,"allergiesCanonical":["Squid","Wheat (Gluten)"]}]}
    
        Input:
        - menuId: 3, ko: "콩국수", en: "Soy Milk Noodles", cleanKo: "콩국수", hints: [NOODLE, SOY]
        Output:
        {"results":[{"menuId":3,"allergiesCanonical":["Soy","Wheat (Gluten)"]}]}
    
        [Actual Input]
        """);
        for (var m : inputs) {
            sb.append("- menuId: ").append(m.get("menuId"))
                    .append(", ko: \"").append(m.get("nameKo")).append("\"")
                    .append(", en: \"").append(m.get("nameEn")).append("\"")
                    .append(", cleanKo: \"").append(m.get("cleanKo")).append("\"");
            @SuppressWarnings("unchecked")
            List<String> hints = (List<String>) m.get("hints");
            if (hints != null && !hints.isEmpty()) {
                sb.append(", hints: ").append(hints);
            }
            sb.append("\n");
        }
        sb.append("""
    
        Return JSON only with:
        {"results":[{"menuId":<long>,"allergiesCanonical":[<string>, ...]}, ...]}
        """);
        return sb.toString();
    }

    private String toJson(String s) {
        try { return om.writeValueAsString(s); } catch (Exception e) { return "\"\""; }
    }
}