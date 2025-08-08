package com.example.ondongnae.backend.category.service;

import com.example.ondongnae.backend.category.dto.SignUpCategoryDto;
import com.example.ondongnae.backend.category.model.MainCategory;
import com.example.ondongnae.backend.category.model.SubCategory;
import com.example.ondongnae.backend.category.repository.MainCategoryRepository;
import com.example.ondongnae.backend.category.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    public CategoryService(MainCategoryRepository mainCategoryRepository, SubCategoryRepository subCategoryRepository) {
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }

    public List<SignUpCategoryDto> getAllMainCategory() {
        List<MainCategory> all = mainCategoryRepository.findAll();

        List<SignUpCategoryDto> mainCategories = new ArrayList<>();
        for (MainCategory mainCategory : all) {
            SignUpCategoryDto dto = SignUpCategoryDto.builder().id(mainCategory.getId())
                    .name(mainCategory.getNameKo()).build();
            mainCategories.add(dto);
        }
        return mainCategories;
    }

    public List<SignUpCategoryDto> getSubCategories(Long id) {
        // 대분류에 해당하는 소분류 조회
        MainCategory mainCategory = mainCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 대분류가 존재하지 않습니다."));
        List<SubCategory> byMainCategory = subCategoryRepository.findByMainCategory(mainCategory);

        List<SignUpCategoryDto> subCategories = new ArrayList<>();
        for (SubCategory subCategory : byMainCategory) {
            SignUpCategoryDto dto = SignUpCategoryDto.builder().id(subCategory.getId())
                    .name(subCategory.getNameKo()).build();
            subCategories.add(dto);
        }
        return subCategories;
    }
}
