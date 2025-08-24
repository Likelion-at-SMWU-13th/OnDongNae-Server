package com.example.ondongnae.backend.course.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="course_option")
public class Option {

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
}
