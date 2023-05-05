package com.seungah.todayclothes.domain.member.repository;

import com.seungah.todayclothes.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	Optional<Member> findByEmail(String email);

    Optional<Member> findByKakaoUserId(Long socialId);

	Optional<Member> findByNaverUserId(String socialId);

}