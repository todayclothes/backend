package com.seungah.todayclothes.domain.scheduleweather.repository;

import com.seungah.todayclothes.domain.scheduleweather.entity.ScheduleWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleWeatherRepository extends JpaRepository<ScheduleWeather, Long> {

}
