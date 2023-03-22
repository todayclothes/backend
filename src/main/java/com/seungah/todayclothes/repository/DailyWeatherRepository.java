package com.seungah.todayclothes.repository;

import com.seungah.todayclothes.entity.DailyWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherRepository extends JpaRepository<DailyWeather, Long> {

}
