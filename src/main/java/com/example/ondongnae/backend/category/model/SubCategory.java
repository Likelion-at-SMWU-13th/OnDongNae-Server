package com.example.ondongnae.backend.category.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nameKo;

    @Column(nullable = false)
    private String nameEn;

    @Column(nullable = false)
    private String nameJa;

    @Column(nullable = false)
    private String nameZh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="main_category_id")
    private MainCategory mainCategory;

    @OneToMany(mappedBy = "subCategory")
    private List<StoreSubCategory> storeSubCategories = new ArrayList<>();

}
