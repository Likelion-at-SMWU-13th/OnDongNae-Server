package com.example.ondongnae.backend.category.model;

import com.example.ondongnae.backend.store.model.Store;
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
public class MainCategory {

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

    @OneToMany(mappedBy = "mainCategory")
    private List<Store> stores = new ArrayList<>();

    @OneToMany(mappedBy = "mainCategory")
    private List<SubCategory> subCategories = new ArrayList<>();

}
