package com.example.ondongnae.backend.market.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Market {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name_en;

    @Column(nullable = false)
    private String name_ja;

    @Column(nullable = false)
    private String name_zh;

    @Column(nullable = false)
    private String address_en;

    @Column(nullable = false)
    private String address_ja;

    @Column(nullable = false)
    private String address_zh;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false, length = 3000)
    private String desc_en;

    @Column(nullable = false, length = 3000)
    private String desc_ja;

    @Column(nullable = false, length = 3000)
    private String desc_zh;

}
