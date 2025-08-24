package com.example.ondongnae.backend.market.repository;

import com.example.ondongnae.backend.market.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findByNameKo(String ko);
    boolean existsById(Long id);
}
