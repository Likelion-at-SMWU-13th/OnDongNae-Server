package com.example.ondongnae.backend.category.repository;

import com.example.ondongnae.backend.category.model.StoreSubCategory;
import com.example.ondongnae.backend.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreSubCategoryRepository extends JpaRepository<StoreSubCategory, Long> {
    List<StoreSubCategory> findByStore(Store store);
}
