package com.example.ondongnae.backend.allergy.heuristic;

import com.example.ondongnae.backend.allergy.cononical.CanonicalAllergy;

import java.util.*;
import java.util.regex.Pattern;

import static com.example.ondongnae.backend.allergy.cononical.CanonicalAllergy.*;


/**
 * 메뉴 텍스트에서 태그를 뽑고, 태그 기반으로 확정적/통계적 알레르기를 유추한다.
 * - 결과는 "캐논명(영문)" 기준.
 * - GPT와 독립적으로 동작하며, 최종 결과는 (휴리스틱 ∪ GPT)로 합친다.
 */
public final class HeuristicAllergyEngine {

    private HeuristicAllergyEngine(){}

    // ===== 키워드 패턴 =====
    private static final Pattern P_NOODLE = Pattern.compile(
            "(국수|면|우동|라면|소바|짜장면|짬뽕|칼국수|비빔국수|냉면|ramen|udon|soba|noodle|pasta)",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern P_RICE_NOODLE = Pattern.compile("(쌀국수|pho|rice\\s*noodle)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_BUCKWHEAT = Pattern.compile("(메밀|소바|soba|buckwheat)", Pattern.CASE_INSENSITIVE);

    private static final Pattern P_FRIED = Pattern.compile("(튀김|전|부침|tempura|fried|batter|breaded)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_CUTLET = Pattern.compile("(돈까스|카츠|katsu|tonkatsu|cutlet)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_DUMPLING = Pattern.compile("(만두|gyoza|dumpling|mandu)", Pattern.CASE_INSENSITIVE);

    private static final Pattern P_EGG = Pattern.compile("(계란|달걀|egg|tamago)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_SESAME = Pattern.compile("(참깨|\\b깨\\b|sesame)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_SOY = Pattern.compile("(콩|두부|soy|tofu|간장|된장|춘장)", Pattern.CASE_INSENSITIVE);

    private static final Pattern P_SQUID = Pattern.compile("(오징어|squid)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_SHRIMP = Pattern.compile("(새우|shrimp)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_CRAB = Pattern.compile("(게\\b|crab)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_SHELLFISH = Pattern.compile("(조개|바지락|홍합|shellfish|clam|mussel|scallop)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_FISH = Pattern.compile("(어묵|생선|fish|가자미|명태|연어)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_MACKEREL = Pattern.compile("(고등어|mackerel)", Pattern.CASE_INSENSITIVE);

    private static final Pattern P_PORK = Pattern.compile("(돼지|돼지고기|pork|tonkatsu)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_BEEF = Pattern.compile("(소고기|beef)", Pattern.CASE_INSENSITIVE);
    private static final Pattern P_CHICKEN = Pattern.compile("(닭|닭고기|chicken)", Pattern.CASE_INSENSITIVE);

    /** 분석 결과(태그 + 휴리스틱 알레르기) */
    public static final class Result {
        private final Set<DishTag> tags;
        private final Set<CanonicalAllergy> allergens;

        public Result(Set<DishTag> tags, Set<CanonicalAllergy> allergens) {
            this.tags = Collections.unmodifiableSet(tags);
            this.allergens = Collections.unmodifiableSet(allergens);
        }
        public Set<DishTag> tags() { return tags; }
        public Set<CanonicalAllergy> allergens() { return allergens; }
    }

    /** 텍스트에서 태그 추출 → 태그 기반 알레르기 유추 */
    public static Result analyze(String nameKo, String nameEn, String cleanKo) {
        String text = String.join(" ", safe(nameKo), safe(nameEn), safe(cleanKo));

        // 1) 태그 추출
        Set<DishTag> tags = new HashSet<>();
        if (P_NOODLE.matcher(text).find()) tags.add(DishTag.NOODLE);
        if (P_RICE_NOODLE.matcher(text).find()) tags.add(DishTag.RICE_NOODLE);
        if (P_BUCKWHEAT.matcher(text).find()) tags.add(DishTag.BUCKWHEAT_BASE);

        if (P_FRIED.matcher(text).find()) tags.add(DishTag.FRIED_OR_BATTER);
        if (P_CUTLET.matcher(text).find()) tags.add(DishTag.CUTLET);
        if (P_DUMPLING.matcher(text).find()) tags.add(DishTag.DUMPLING);

        if (P_EGG.matcher(text).find()) tags.add(DishTag.EGG);
        if (P_SESAME.matcher(text).find()) tags.add(DishTag.SESAME);
        if (P_SOY.matcher(text).find()) tags.add(DishTag.SOY);

        if (P_SQUID.matcher(text).find()) tags.add(DishTag.SQUID);
        if (P_SHRIMP.matcher(text).find()) tags.add(DishTag.SHRIMP);
        if (P_CRAB.matcher(text).find()) tags.add(DishTag.CRAB);
        if (P_SHELLFISH.matcher(text).find()) tags.add(DishTag.SHELLFISH);
        if (P_FISH.matcher(text).find()) tags.add(DishTag.FISH);
        if (P_MACKEREL.matcher(text).find()) tags.add(DishTag.MACKEREL);

        if (P_PORK.matcher(text).find()) tags.add(DishTag.PORK);
        if (P_BEEF.matcher(text).find()) tags.add(DishTag.BEEF);
        if (P_CHICKEN.matcher(text).find()) tags.add(DishTag.CHICKEN);

        // 2) 태그 → 휴리스틱 알레르기 매핑
        Set<CanonicalAllergy> out = new HashSet<>();

        // 면류 기본: 글루텐. 단, 쌀국수는 제외
        if (tags.contains(DishTag.NOODLE) && !tags.contains(DishTag.RICE_NOODLE)) {
            out.add(WHEAT_GLUTEN);
        }
        // 메밀(소바): Buckwheat. 한국/일본 다수 제품은 밀 혼합 → 보수적으로 글루텐도 함께
        if (tags.contains(DishTag.BUCKWHEAT_BASE)) {
            out.add(BUCKWHEAT);
            out.add(WHEAT_GLUTEN);
        }
        // 튀김/전/부침/빵가루: 글루텐
        if (tags.contains(DishTag.FRIED_OR_BATTER) || tags.contains(DishTag.CUTLET) || tags.contains(DishTag.DUMPLING)) {
            out.add(WHEAT_GLUTEN);
        }
        // 돈까스: 돼지고기 + 글루텐 + (종종) 계란
        if (tags.contains(DishTag.CUTLET)) {
            out.add(PORK);
            out.add(WHEAT_GLUTEN);
            out.add(EGGS);
        }
        // 만두: 보편적으로 밀피(글루텐)
        if (tags.contains(DishTag.DUMPLING)) {
            out.add(WHEAT_GLUTEN);
        }
        // 콩/두부/간장/된장: 대두
        if (tags.contains(DishTag.SOY)) {
            out.add(SOY);
        }
        // 참깨: Sesame
        if (tags.contains(DishTag.SESAME)) {
            out.add(SESAME);
        }
        // 개별 해산물
        if (tags.contains(DishTag.SQUID)) out.add(SQUID);
        if (tags.contains(DishTag.SHRIMP)) out.add(SHRIMP);
        if (tags.contains(DishTag.CRAB)) out.add(CRAB);
        if (tags.contains(DishTag.SHELLFISH)) out.add(SHELLFISH);
        if (tags.contains(DishTag.FISH)) out.add(FISH);
        if (tags.contains(DishTag.MACKEREL)) out.add(MACKEREL);

        // 육류
        if (tags.contains(DishTag.PORK)) out.add(PORK);
        if (tags.contains(DishTag.BEEF)) out.add(BEEF);
        if (tags.contains(DishTag.CHICKEN)) out.add(CHICKEN);

        // 계란
        if (tags.contains(DishTag.EGG)) out.add(EGGS);

        return new Result(tags, out);
    }

    private static String safe(String s){ return (s==null)?"":s; }
}
