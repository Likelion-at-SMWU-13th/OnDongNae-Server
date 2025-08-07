package com.example.ondongnae.backend.category.repository;

import com.example.ondongnae.backend.category.model.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    boolean existsById(Long id);
}
