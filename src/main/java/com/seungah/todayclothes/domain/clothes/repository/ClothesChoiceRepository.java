package com.seungah.todayclothes.domain.clothes.repository;

import com.seungah.todayclothes.domain.clothes.entity.ClothesChoice;
import com.seungah.todayclothes.domain.member.entity.Member;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesChoiceRepository extends JpaRepository<ClothesChoice, Long> {

	@EntityGraph(value = "clothesChoiceWithAssociations", type = EntityGraph.EntityGraphType.LOAD)
	List<ClothesChoice> findAllByMember(Member member);

	void deleteAllByScheduleDetail(ScheduleDetail scheduleDetail);


}
