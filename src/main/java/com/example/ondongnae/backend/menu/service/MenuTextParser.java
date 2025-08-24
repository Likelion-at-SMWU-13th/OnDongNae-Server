package com.example.ondongnae.backend.menu.service;

import com.example.ondongnae.backend.menu.dto.OcrExtractItemDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 한 줄을 여러 (name, price) 페어로 분해해서 추출
 * 수량(3개 등)은 가격 후보에서 제외하고 이름에는 남김
 * 가격 우선순위: 통화 표기 > 콤마 포함 > 자리수>=4(>=1000) > 하한선(>=500)
 */
@Component
public class MenuTextParser {

    // 숫자 토큰: (숫자)(통화)?  |  (수량숫자)(수량단위)
    private static final Pattern NUM_TOKEN = Pattern.compile(
            "(\\d{1,3}(?:,\\d{3})+|\\d+)\\s*(원|₩|￦|krw|won)?"
                    + "|(\\d+)\\s*(개|pcs|份|个|碗|잔|장|인분|그릇)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Set<String> CURRENCY = Set.of("원","₩","￦","krw","won");
    private static final Set<String> QTY_UNIT = Set.of("개","pcs","份","个","碗","잔","장","인분","그릇");

    // 사이즈 토큰(대/중/소/大/中/小) - 괄호 포함/미포함
    private static final Pattern SIZE_TOKEN = Pattern.compile("(?:\\(|\\s|^)(대|중|소|大|中|小)(?:\\)|\\s|$)");

    public List<OcrExtractItemDto> parse(String raw) {
        if (raw == null || raw.isBlank()) return List.of();

        String[] lines = raw.replace('\u00A0', ' ').split("\\r?\\n");
        List<OcrExtractItemDto> out = new ArrayList<>();

        for (String origin : lines) {
            String line = normalize(origin);
            if (line.isBlank()) continue;

            // 한 줄에서 여러 (name, price) 페어를 뽑음
            out.addAll(extractPairsFromLine(line));
        }
        return out;
    }

    // 한 줄에서 [이름 ... 가격] 페어를 여러 개 추출
    private List<OcrExtractItemDto> extractPairsFromLine(String line) {
        List<OcrExtractItemDto> result = new ArrayList<>();

        // 사이즈 토큰은 전역 제거 대신, 세그먼트별로 발견되면 이름에만 반영하기 위해
        // 일단 원문 라인에서 숫자 토큰 위치만 수집
        Matcher m = NUM_TOKEN.matcher(line);
        List<Token> tokens = new ArrayList<>();
        while (m.find()) {
            String num = m.group(1);
            String cur = m.group(2);
            String qtyNum = m.group(3);
            String qtyUnit = m.group(4);

            if (num != null) {
                tokens.add(Token.number(m.start(), m.end(), num, cur));
            } else if (qtyNum != null && qtyUnit != null) {
                tokens.add(Token.quantity(m.start(), m.end(), qtyNum, qtyUnit));
            }
        }

        // 토큰이 없으면: 가격 없는 단독 이름일 수 있으니 전체를 한 항목으로 (price=null)
        if (tokens.isEmpty()) {
            String name = applySizeSuffix(line.trim());
            if (!isNoise(name)) result.add(new OcrExtractItemDto(name, null));
            return result;
        }

        // 왼쪽 → 오른쪽으로 스캔하며 "가격 후보" 토큰을 만날 때마다 직전 세그먼트를 이름으로
        int cut = 0;
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.type != TokenType.NUMBER) continue;                 // 수량 토큰은 건너뜀
            if (!isPriceCandidate(t.num, t.currency)) continue;       // 가격 조건 미충족이면 스킵

            // 이름 후보: 직전 컷 ~ 가격 토큰 시작
            String nameSeg = line.substring(cut, t.start).trim();
            nameSeg = applySizeSuffix(nameSeg);
            if (!isNoise(nameSeg)) {
                Integer price = parsePrice(t.num);
                result.add(new OcrExtractItemDto(nameSeg, price));
            }

            // 다음 세그먼트 시작 지점을 가격 토큰 끝으로 이동
            cut = t.end;
        }

        // 마지막으로 잘린 뒤에 남은 꼬리 텍스트가 의미 있는 "이름"이라면 price=null로 추가
        String tail = line.substring(cut).trim();
        tail = applySizeSuffix(tail);
        if (!tail.isBlank() && !isNoise(tail)) {
            result.add(new OcrExtractItemDto(tail, null));
        }

        return result;
    }

    // 가격 후보 판단 규칙
    private boolean isPriceCandidate(String num, String currencyRaw) {
        if (num == null) return false;

        // 1) 통화 동반
        if (currencyRaw != null) {
            String c = currencyRaw.toLowerCase(Locale.ROOT);
            if (CURRENCY.contains(c) || CURRENCY.contains(currencyRaw)) return true;
        }
        // 2) 콤마 포함(천단위)
        if (num.contains(",")) return true;

        // 3) 자리수 >= 4(>=1000)
        String plain = num.replace(",", "");
        if (plain.length() >= 4) return true;

        // 4) 하한선 (>= 500)
        Integer p = parsePrice(num);
        return p != null && p >= 500;
    }

    private Integer parsePrice(String s) {
        try { return Integer.parseInt(s.replace(",", "")); }
        catch (Exception e) { return null; }
    }

    // 유틸

    private String normalize(String s) {
        String noEmoji = s.replaceAll("[\\p{So}\\p{Cn}]", " ");
        return noEmoji.replaceAll("[\\t\\r]", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }

    // 세그먼트 내의 사이즈 토큰(대/중/소/大/中/小)을 검출해 이름에 괄호로 붙임
    private String applySizeSuffix(String seg) {
        if (seg == null || seg.isBlank()) return seg;
        Matcher m = SIZE_TOKEN.matcher(seg);
        if (m.find()) {
            String raw = m.group(1);
            String size = switch (raw) {
                case "大" -> "대";
                case "中" -> "중";
                case "小" -> "소";
                default -> raw;
            };
            String cleaned = SIZE_TOKEN.matcher(seg).replaceAll(" ").replaceAll("\\s{2,}", " ").trim();
            if (!cleaned.isBlank()) return cleaned + " (" + size + ")";
        }
        return seg;
    }

    private boolean isNoise(String name) {
        if (name == null) return true;
        String n = name.trim();
        if (n.isEmpty()) return true;
        if (n.length() == 1 && !Character.isDigit(n.charAt(0))) return true;
        return false;
    }

    // 내부 자료구조
    private enum TokenType { NUMBER, QUANTITY }

    private static class Token {
        final TokenType type;
        final int start, end;
        final String num;       // NUMBER: 숫자, QUANTITY: 수량 숫자
        final String currency;  // NUMBER에서만 사용

        private Token(TokenType type, int start, int end, String num, String currency) {
            this.type = type; this.start = start; this.end = end; this.num = num; this.currency = currency;
        }
        static Token number(int s, int e, String num, String currency) {
            return new Token(TokenType.NUMBER, s, e, num, currency);
        }
        static Token quantity(int s, int e, String qtyNum, String qtyUnit) {
            // 수량은 가격 결정에서 제외(이름에만 남게 함). qtyUnit은 사용하지 않음
            return new Token(TokenType.QUANTITY, s, e, qtyNum, null);
        }
    }
}