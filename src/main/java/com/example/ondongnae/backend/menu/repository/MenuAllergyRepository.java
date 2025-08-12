package com.example.ondongnae.backend.menu.repository;

import com.example.ondongnae.backend.menu.model.MenuAllergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuAllergyRepository extends JpaRepository<MenuAllergy, Long> {

    @Modifying
    @Query("DELETE FROM MenuAllergy ma WHERE ma.menu.id IN :menuIds")
    void deleteByMenuIds(@Param("menuIds") List<Long> menuIds);
}
