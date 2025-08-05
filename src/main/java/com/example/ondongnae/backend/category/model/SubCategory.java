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
    private String name_ko;

    @Column(nullable = false)
    private String name_en;

    @Column(nullable = false)
    private String name_ja;

    @Column(nullable = false)
    private String name_zh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="main_category_id")
    private MainCategory mainCategory;

    @OneToMany(mappedBy = "subCategory")
    private List<StoreSubCategory> storeSubCategories = new ArrayList<>();

}
