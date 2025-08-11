package com.example.ondongnae.backend.course.repository;

import com.example.ondongnae.backend.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT * FROM course ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Course> pickRandom();

}
