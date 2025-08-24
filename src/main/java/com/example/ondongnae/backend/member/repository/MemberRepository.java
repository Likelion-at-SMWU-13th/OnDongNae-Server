package com.example.ondongnae.backend.member.repository;

import com.example.ondongnae.backend.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByPhone(String phone);
    Member findByLoginId(String login_id);
    Boolean existsByLoginId(String login_id);
}
