package com.example.ondongnae.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "store")
    private List<CourseStore> courseStores =  new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="main_category_id")
    private MainCategory mainCategory;

    @OneToMany(mappedBy = "store")
    private List<StoreSubCategory> storeSubCategories = new ArrayList<>();

}
