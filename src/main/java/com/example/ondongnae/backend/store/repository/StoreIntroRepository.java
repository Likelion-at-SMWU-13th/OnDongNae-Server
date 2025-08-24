package com.example.ondongnae.backend.store.repository;

import com.example.ondongnae.backend.store.model.StoreIntro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreIntroRepository extends JpaRepository<StoreIntro, Long> {
    // 특정 가게의 다국어 소개(짧은/긴) 조회
    Optional<StoreIntro> findFirstByStoreIdAndLang(Long storeId, String lang);
}
