package com.seungah.todayclothes.domain.schedule.repository;

import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import com.seungah.todayclothes.domain.schedule.entity.ScheduleDetail;
import com.seungah.todayclothes.global.type.TimeOfDay;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleDetailRepository extends JpaRepository<ScheduleDetail, Long> {

	ScheduleDetail findByScheduleAndTimeOfDay(Schedule schedule, TimeOfDay timeOfDay);

	List<ScheduleDetail> findAllBySchedule(Schedule schedule);
}
