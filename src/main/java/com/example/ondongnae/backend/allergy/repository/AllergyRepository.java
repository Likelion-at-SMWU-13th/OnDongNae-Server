package com.example.ondongnae.backend.allergy.repository;

import com.example.ondongnae.backend.allergy.model.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    List<Allergy> findByLabelEnIn(Collection<String> labelEns);
}
