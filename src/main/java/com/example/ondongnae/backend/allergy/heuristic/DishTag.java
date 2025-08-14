package com.example.ondongnae.backend.allergy.heuristic;

// 메뉴명에서 추출하는 조리/식품 태그
public enum DishTag {
    NOODLE,         // 국수/면/라면/우동/소바/짜장면/칼국수/파스타...
    RICE_NOODLE,    // 쌀국수/pho/rice noodle
    BUCKWHEAT_BASE, // 메밀/소바/soba/buckwheat
    FRIED_OR_BATTER,// 튀김/전/부침/tempura/fried/batter/breaded
    CUTLET,         // 돈까스/카츠/katsu/tonkatsu/cutlet
    DUMPLING,       // 만두/gyoza/dumpling/mandu
    SEAFOOD,        // 해물/해산물
    SQUID, SHRIMP, CRAB, SHELLFISH, FISH, MACKEREL,
    PORK, BEEF, CHICKEN,
    EGG,            // 계란/달걀/egg/tamago
    SOY,            // 콩/두부/간장/된장/soy/tofu
    SESAME          // 참깨/깨/sesame
}
