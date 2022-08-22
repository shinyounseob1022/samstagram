package com.sparta.samstagram.repository;

import com.sparta.samstagram.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findById(Long id);
  Optional<Member> findByMemberId(String memberId);
  Optional<Member> findByNickname(String nickname);
}
