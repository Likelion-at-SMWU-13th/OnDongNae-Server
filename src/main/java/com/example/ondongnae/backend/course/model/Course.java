package com.example.ondongnae.backend.course.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String longDescriptionKo;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String longDescriptionEn;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String longDescriptionZh;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String longDescriptionJa;

    @Column(nullable = false)
    private String shortDescriptionKo;

    @Column(nullable = false)
    private String shortDescriptionEn;

    @Column(nullable = false)
    private String shortDescriptionZh;

    @Column(nullable = false)
    private String shortDescriptionJa;

    @OneToMany(mappedBy = "course")
    private List<CourseStore> courseStores = new ArrayList<>();

}
