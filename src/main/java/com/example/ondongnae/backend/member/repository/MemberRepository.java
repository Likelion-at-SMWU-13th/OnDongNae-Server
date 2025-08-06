package com.example.ondongnae.backend.member.repository;

import com.example.ondongnae.backend.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public boolean existsByPhone(String phone);
}
