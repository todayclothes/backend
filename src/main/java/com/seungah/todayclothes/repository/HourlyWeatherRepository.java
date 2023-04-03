package com.seungah.todayclothes.repository;

import com.seungah.todayclothes.entity.HourlyWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, Long> {

}
