package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.clothes.repository.queryDsl.ClothesChoiceQueryRepository;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesChoiceRepository extends JpaRepository<ClothesChoice, Long>,
	ClothesChoiceQueryRepository {

	void deleteAllByScheduleDetail(ScheduleDetail scheduleDetail);

}
