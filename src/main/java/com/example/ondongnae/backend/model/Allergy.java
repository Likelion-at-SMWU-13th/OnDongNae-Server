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

    private String label_ko;
    private String label_en;
    private String label_ja;
    private String label_zh;

    @OneToMany(mappedBy = "allergy")
    private List<MenuAllergy> menuAllergies = new ArrayList<>();
}
