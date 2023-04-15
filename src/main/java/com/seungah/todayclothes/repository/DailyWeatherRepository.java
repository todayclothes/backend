package com.seungah.todayclothes.repository;

import com.seungah.todayclothes.entity.DailyWeather;
import com.seungah.todayclothes.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DailyWeatherRepository extends JpaRepository<DailyWeather, Long> {
    List<DailyWeather> findByDateAndRegion(LocalDateTime date, Region region);

    List<DailyWeather> findByRegion(Region region);
}
