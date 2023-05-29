package com.seungah.todayclothes.domain.member.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.member.entity.Likes;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.queryDsl.LikesQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long>, LikesQueryRepository {

	boolean existsByClothesChoiceAndMember(ClothesChoice clothesChoice, Member member);

	@Modifying
	@Query("delete from Likes l where l.id = :id")
	void deleteById(@Param("id") Long id);
}
