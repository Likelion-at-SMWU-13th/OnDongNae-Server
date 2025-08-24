package com.example.ondongnae.backend.course.repository;


import com.example.ondongnae.backend.course.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
