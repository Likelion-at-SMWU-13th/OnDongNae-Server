package com.example.ondongnae.backend.store.repository;

import com.example.ondongnae.backend.store.model.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // 이미지/시장까지만 가져오기 (메뉴 제외)
    @EntityGraph(attributePaths = {
            "storeImages",
            "market",
            "member"
    })
    Optional<Store> findById(Long id);

    Optional<Store> findByMemberId(Long member_Id);
}
