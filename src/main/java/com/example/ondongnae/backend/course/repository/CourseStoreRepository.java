package com.example.ondongnae.backend.course.repository;

import com.example.ondongnae.backend.course.model.CourseStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStoreRepository extends JpaRepository<CourseStore, Long> {
    List<CourseStore> findByCourseId(Long courseId);
}
