package com.example.ondongnae.backend.store.repository;

import com.example.ondongnae.backend.store.model.StoreIntro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreIntroRepository extends JpaRepository<StoreIntro, Long> {
}
