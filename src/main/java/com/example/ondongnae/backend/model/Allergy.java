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
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label_ko;
    @Column(nullable = false)
    private String label_en;
    @Column(nullable = false)
    private String label_ja;
    @Column(nullable = false)
    private String label_zh;

    @OneToMany(mappedBy = "allergy")
    private List<MenuAllergy> menuAllergies = new ArrayList<>();
}
