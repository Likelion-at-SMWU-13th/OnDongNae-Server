package com.example.ondongnae.backend.store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreIntro {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String lang;

    @Column(nullable = false)
    private String tagline;

    @Column(nullable = false, length = 1000)
    private String short_intro;

    @Column(nullable = false, length = 3000)
    private String long_intro;
}
