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
    private String title_ko;

    @Column(nullable = false)
    private String title_en;

    @Column(nullable = false)
    private String title_zh;

    @Column(nullable = false)
    private String title_ja;

    @Column(nullable = false)
    private String description_ko;

    @Column(nullable = false)
    private String description_en;

    @Column(nullable = false)
    private String description_zh;

    @Column(nullable = false)
    private String description_ja;

    @OneToMany(mappedBy = "course")
    private List<CourseStore> courseStores = new ArrayList<>();

}
