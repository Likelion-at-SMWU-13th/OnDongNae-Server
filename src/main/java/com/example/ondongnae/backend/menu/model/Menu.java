package com.example.ondongnae.backend.menu.model;

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
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "menu")
    private List<MenuAllergy> menuAllergies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name_ko;

    @Column(nullable = false)
    private String name_en;

    @Column(nullable = false)
    private String name_zh;

    @Column(nullable = false)
    private String name_ja;

    @Column(nullable = false)
    private int price_krw;

}
