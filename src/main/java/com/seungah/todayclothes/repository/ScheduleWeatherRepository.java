package com.seungah.todayclothes.repository;

import com.seungah.todayclothes.entity.ScheduleWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleWeatherRepository extends JpaRepository<ScheduleWeather, Long> {

}
