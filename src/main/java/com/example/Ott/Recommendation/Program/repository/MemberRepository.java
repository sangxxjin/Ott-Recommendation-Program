package com.example.Ott.Recommendation.Program.repository;

import com.example.Ott.Recommendation.Program.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MemberRepository extends JpaRepository<Member, String> {
  Optional<Member> findByEmail(String email);
}
