package com.example.ondongnae.backend.store.repository;

import com.example.ondongnae.backend.store.model.Store;
import com.example.ondongnae.backend.store.model.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
    StoreImage findFirstByStoreOrderByOrderAsc(Store store);
}
