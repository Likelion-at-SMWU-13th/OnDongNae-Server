package com.example.ondongnae.backend.store.model;

import com.example.ondongnae.backend.course.model.CourseStore;
import com.example.ondongnae.backend.market.model.Market;
import com.example.ondongnae.backend.member.model.Member;
import com.example.ondongnae.backend.menu.model.Menu;
import com.example.ondongnae.backend.category.model.MainCategory;
import com.example.ondongnae.backend.category.model.StoreSubCategory;
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
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "market_id", nullable = false)
    private Market market;

    @OneToMany(mappedBy = "store")
    private List<CourseStore> courseStores =  new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Menu> menus = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="main_category_id")
    private MainCategory mainCategory;

    @OneToMany(mappedBy = "store")
    private List<StoreImage> storeImages = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<StoreSubCategory> storeSubCategories = new ArrayList<>();

    @Column(nullable = false)
    private String name_ko;

    @Column(nullable = false)
    private String name_en;

    @Column(nullable = false)
    private String name_ja;

    @Column(nullable = false)
    private String name_zh;

    @Column(nullable = false)
    private String address_ko;

    @Column(nullable = false)
    private String address_en;

    @Column(nullable = false)
    private String address_ja;

    @Column(nullable = false)
    private String address_zh;

    private String phone;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

}
