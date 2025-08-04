package com.example.ondongnae.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title_ko;
    private String title_en;
    private String title_zh;
    private String title_ja;

    private String description_ko;
    private String description_en;
    private String description_zh;
    private String description_ja;

    @OneToMany(mappedBy = "course")
    private List<CourseStore> courseStores = new ArrayList<>();

}
