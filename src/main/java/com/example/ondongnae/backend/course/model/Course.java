package com.example.ondongnae.backend.course.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titleKo;

    @Column(nullable = false)
    private String titleEn;

    @Column(nullable = false)
    private String titleZh;

    @Column(nullable = false)
    private String titleJa;

    @Column(nullable = false)
    private String descriptionKo;

    @Column(nullable = false)
    private String descriptionEn;

    @Column(nullable = false)
    private String descriptionZh;

    @Column(nullable = false)
    private String descriptionJa;

    @OneToMany(mappedBy = "course")
    private List<CourseStore> courseStores = new ArrayList<>();

}
