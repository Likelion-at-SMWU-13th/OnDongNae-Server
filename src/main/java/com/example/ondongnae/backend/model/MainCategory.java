package com.example.ondongnae.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
