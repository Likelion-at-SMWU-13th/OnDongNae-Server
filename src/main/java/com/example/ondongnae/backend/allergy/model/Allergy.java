package com.example.ondongnae.backend.allergy.model;

import com.example.ondongnae.backend.menu.model.MenuAllergy;
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
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String labelKo;

    @Column(nullable = false)
    private String labelEn;

    @Column(nullable = false)
    private String labelJa;

    @Column(nullable = false)
    private String labelZh;

    @OneToMany(mappedBy = "allergy")
    private List<MenuAllergy> menuAllergies = new ArrayList<>();
}
