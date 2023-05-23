package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesChoiceRepository extends JpaRepository<ClothesChoice, Long>, ClothesChoiceQueryRepository {

	void deleteAllByScheduleDetail(ScheduleDetail scheduleDetail);

	@Query("SELECT l.clothesChoice FROM Likes l WHERE l.member = :member")
	List<ClothesChoice> findLikeClothesChoicesByMember(@Param("member") Member member);

}
