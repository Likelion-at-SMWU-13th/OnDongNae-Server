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
    private String nameKo;

    @Column(nullable = false)
    private String nameEn;

    @Column(nullable = false)
    private String nameJa;

    @Column(nullable = false)
    private String nameZh;

    @Column(nullable = false)
    private String addressEn;

    @Column(nullable = false)
    private String addressJa;

    @Column(nullable = false)
    private String addressZh;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false, length = 3000)
    private String descEn;

    @Column(nullable = false, length = 3000)
    private String descJa;

    @Column(nullable = false, length = 3000)
    private String descZh;

}
