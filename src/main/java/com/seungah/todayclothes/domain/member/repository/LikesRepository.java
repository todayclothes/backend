package com.seungah.todayclothes.domain.member.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.member.entity.Likes;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.member.repository.queryDsl.LikesQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long>, LikesQueryRepository {

	boolean existsByClothesChoiceAndMember(ClothesChoice clothesChoice, Member member);

	void deleteByClothesChoiceIdAndMemberId(Long clothesChoiceId, Long userId);
}
