package com.seungah.todayclothes.domain.member.repository;

import com.seungah.todayclothes.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m JOIN FETCH m.region WHERE m.id = :id")
	Optional<Member> findByIdWithRegion(@Param("id") Long id);

	@Query("SELECT m FROM Member m JOIN FETCH m.clothesTypeWeights WHERE m.id = :id")
	Member findByIdWithClothesTypeWeights(@Param("id") Long id);

	boolean existsByEmail(String email);

	Optional<Member> findByEmail(String email);

    Optional<Member> findByKakaoUserId(Long socialId);

	Optional<Member> findByNaverUserId(String socialId);

}
