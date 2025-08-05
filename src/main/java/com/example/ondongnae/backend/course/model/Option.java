package com.example.ondongnae.backend.course.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name_ko;

    @Column(nullable = false)
    private String name_en;

    @Column(nullable = false)
    private String name_ja;

    @Column(nullable = false)
    private String name_zh;
}
