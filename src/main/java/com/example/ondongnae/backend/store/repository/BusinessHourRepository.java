package com.example.ondongnae.backend.store.repository;

import com.example.ondongnae.backend.store.model.BusinessHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {
    // 특정 가게의 주간 영업시간 목록 조회
    List<BusinessHour> findByStoreId(Long storeId);
    void deleteByStoreId(Long storeId);
}

