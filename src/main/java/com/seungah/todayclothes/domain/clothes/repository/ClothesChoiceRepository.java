package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.ClothesChoiceQueryRepository;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesChoiceRepository extends JpaRepository<ClothesChoice, Long>,
	ClothesChoiceQueryRepository {

	@Modifying
	@Query("DELETE FROM ClothesChoice c WHERE c.scheduleDetail.id = :scheduleDetailId")
	void deleteAllByScheduleDetailId(@Param("scheduleDetailId") Long scheduleDetailId);

	void deleteAllByScheduleDetail(ScheduleDetail scheduleDetail);

}
