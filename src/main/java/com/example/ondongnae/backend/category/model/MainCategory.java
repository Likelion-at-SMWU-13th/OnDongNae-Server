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
    private String nameKo;

    @Column(nullable = false)
    private String nameEn;

    @Column(nullable = false)
    private String nameJa;

    @Column(nullable = false)
    private String nameZh;

    @OneToMany(mappedBy = "mainCategory")
    private List<Store> stores = new ArrayList<>();

    @OneToMany(mappedBy = "mainCategory")
    private List<SubCategory> subCategories = new ArrayList<>();

}
