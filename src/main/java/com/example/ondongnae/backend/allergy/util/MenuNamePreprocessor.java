package com.example.ondongnae.backend.allergy.util;

/**
 * 메뉴명 전처리 유틸
 * 가격/수량/사이즈/괄호/이모지/특수문자 제거
 * 공백 정규화
 * 모델이 핵심 키워드(재료/조리법 등)에 집중하도록 노이즈를 제거함
 */
public final class MenuNamePreprocessor {
    private MenuNamePreprocessor() {}

    public static String clean(String src) {
        if (src == null) return "";
        String s = src;

        // 가격: "5,000원" 등 제거
        s = s.replaceAll("\\d{1,3}(,\\d{3})*\\s*원", " ");
        // 수량: "2개/2 pcs/2장/2인분/2팩" 등 제거
        s = s.replaceAll("\\d+\\s*(개|pcs|장|인분|팩)", " ");
        // 사이즈: L/M/S/대/중/소 제거
        s = s.replaceAll("\\b(L|M|S|대|중|소)\\b", " ");

        // 괄호 내용 제거
        s = s.replaceAll("\\(.*?\\)", " ");
        s = s.replaceAll("\\[.*?\\]", " ");

        // 이모지/특수문자 제거(한글/영문/숫자/공백/일부기호만 남김)
        s = s.replaceAll("[^\\p{IsHangul}\\p{Alnum}\\s/·-]", " ");

        // 공백 정규화
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }
}
