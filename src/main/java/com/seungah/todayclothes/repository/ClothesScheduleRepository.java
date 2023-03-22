package com.seungah.todayclothes.repository;

import com.seungah.todayclothes.entity.ClothesSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesScheduleRepository extends JpaRepository<ClothesSchedule, Long> {

}
