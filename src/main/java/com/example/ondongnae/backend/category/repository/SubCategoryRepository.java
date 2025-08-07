package com.example.ondongnae.backend.category.repository;

import com.example.ondongnae.backend.category.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    boolean existsById(Long id);
}
