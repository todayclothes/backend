package com.seungah.todayclothes.domain.schedule.repository;

import com.seungah.todayclothes.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByMemberId(Long memberId);
}
