package com.example.ondongnae.backend.category.repository;

import com.example.ondongnae.backend.category.model.MainCategory;
import com.example.ondongnae.backend.category.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    boolean existsById(Long id);
    List<SubCategory> findByMainCategory(MainCategory mainCategory);
}
