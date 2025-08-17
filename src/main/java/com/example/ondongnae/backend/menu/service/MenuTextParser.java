package com.example.ondongnae.backend.menu.service;

import com.example.ondongnae.backend.menu.dto.OcrExtractItemDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OCR 원문 텍스트를 한 줄씩 파싱해 (메뉴명, 가격) DTO로 생성
 * 괄호/수량/옵션/이모지 등 잡값 제거
 * 가격 패턴: 5,000원 / 7000 / 7000원 / 7,000 won / 7000 krw
 * 가격 미검출은 null 허용(프론트 인라인 수정에서 입력)
 */
@Component
public class MenuTextParser {

    // 가격: 1~3자리 + (콤마 + 3자리)* 또는 단순 숫자, 뒤에 통화표기 옵션
    // 예) 9,000 / 15000 / 3,000원 / 1000 KRW / 7,000 won
    private static final Pattern PRICE = Pattern.compile(
            "(\\d{1,3}(?:,\\d{3})+|\\d+)\\s*(?:원|₩|￦|krw|won)?",
            Pattern.CASE_INSENSITIVE);
    // 라인 끝이나 괄호/공백 사이에 오는 사이즈 토큰들
    // (한글/한자 혼용, '대/중/소', '大/中/小', 그리고 흔한 오인식: C -> 중으로 매핑)
    private static final Pattern SIZE_TOKEN = Pattern.compile("(?:\\(|\\s|^)(대|중|소|大|中|小|C)(?:\\)|\\s|$)");

    public List<OcrExtractItemDto> parse(String raw) {
        if (raw == null || raw.isBlank()) return List.of();

        // 줄 단위 분리 (OCR lineBreak 기준으로 들어옴)
        String[] lines = raw.replace('\u00A0', ' ').split("\\r?\\n");

        List<OcrExtractItemDto> result = new ArrayList<>();
        String pendingName = null;      // 가격 없는 이름을 임시 보관
        String pendingSize = null;      // 라인에서 분리된 사이즈 토큰(중/대/小/中/大/C)

        for (String originalLine : lines) {
            String line = normalize(originalLine);
            if (line.isBlank()) continue;

            // (1) 사이즈 토큰 분리(있으면 보관)
            //  예) "소머리수육 中" -> name="소머리수육", size="중"
            String size = extractSize(line);
            if (size != null) {
                pendingSize = size;
                line = removeSize(line); // 사이즈 텍스트 제거
                line = line.trim();
            }

            // (2) 가격 매칭 시도
            Matcher m = PRICE.matcher(line);
            if (m.find()) {
                // 가격 추출 -> 쉼표 제거
                String num = m.group(1).replace(",", "");
                Integer price = safeParseInt(num);

                // 가격 앞부분을 이름으로 (뒤의 꼬리 텍스트는 버림)
                String name = line.substring(0, m.start()).trim();

                // 이름이 비어있으면, 이전 줄에 있던 이름을 사용
                if (name.isBlank() && pendingName != null) {
                    name = pendingName;
                    pendingName = null; // 소모
                }

                // 사이즈 표기 보존 (예: "소머리수육 (중)")
                if (pendingSize != null) {
                    name = appendSize(name, pendingSize);
                    pendingSize = null;
                }

                // 이름 최소 길이 필터 (오인식 한 글자 제거)
                if (isNoise(name)) continue;

                if (!name.isBlank()) {
                    result.add(new OcrExtractItemDto(name, price));
                }
                continue; // 다음 라인
            }

            // (3) 이 라인에는 가격이 없고, 유의미한 이름인 경우 → 다음 줄 가격과 결합하기 위해 보관
            if (!isNoise(line)) {
                // 만약 이전에 보관된 이름이 남아있다면 먼저 추가(가격 null 허용)
                // -> "소주" 같은 라인들이 가격이 다른 줄에 없어도 남도록
                if (pendingName != null && !pendingName.equals(line)) {
                    // 이전 보관값을 가격 null로 기록
                    result.add(new OcrExtractItemDto(
                            pendingSize != null ? appendSize(pendingName, pendingSize) : pendingName,
                            null));
                    pendingSize = null;
                }
                pendingName = line;
            }
        }

        // 끝났는데 pendingName 남아있으면 기록(가격 null)
        if (pendingName != null && !isNoise(pendingName)) {
            result.add(new OcrExtractItemDto(
                    pendingSize != null ? appendSize(pendingName, pendingSize) : pendingName,
                    null));
        }

        return result;
    }

    /** 공백/기호 정리. 콤마(,)는 여기서 건드리지 않는다! (가격 정규식에서 다룸) */
    private String normalize(String s) {
        // 이모지/기호류 제거 (필요시 축소 가능)
        String noEmoji = s.replaceAll("[\\p{So}\\p{Cn}]", " ");
        // 괄호 일단 보존(사이즈 토큰 추출에 쓰일 수 있음)
        String trimmed = noEmoji
                .replaceAll("[\\t\\r]", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();
        return trimmed;
    }

    /** 한 글자 잡값/노이즈 라인 필터 */
    private boolean isNoise(String name) {
        if (name == null) return true;
        String n = name.trim();
        if (n.isEmpty()) return true;
        // 'C' 같은 한 글자 노이즈 제거
        if (n.length() == 1 && !Character.isDigit(n.charAt(0))) return true;
        // 불필요한 접두/접미만 남은 경우
        return false;
    }

    /** "중/대/小/中/大/C" 같은 사이즈 토큰을 (가능하면) 추출 */
    private String extractSize(String line) {
        Matcher m = SIZE_TOKEN.matcher(line);
        if (m.find()) {
            String raw = m.group(1);
            return normalizeSize(raw);
        }
        return null;
    }

    /** 라인에서 사이즈 토큰 제거 */
    private String removeSize(String line) {
        return SIZE_TOKEN.matcher(line).replaceAll(" ").replaceAll("\\s{2,}", " ");
    }

    /** 'C' 오인식 → '중'으로 보정, 한자 → 한글 */
    private String normalizeSize(String s) {
        switch (s) {
            case "大": return "대";
            case "中": return "중";
            case "小": return "소";
            case "C":  return "중"; // 흔한 오탐 보정
            default:   return s;    // 대/중/소
        }
    }

    /** 이름 뒤에 사이즈를 괄호로 표기 */
    private String appendSize(String name, String size) {
        name = (name == null ? "" : name.trim());
        if (name.isEmpty()) return name;
        return name + " (" + size + ")";
    }

    private Integer safeParseInt(String n) {
        try {
            return Integer.parseInt(n);
        } catch (Exception e) {
            return null;
        }
    }
}

