package com.seungah.todayclothes.domain.weather.repository;

import com.seungah.todayclothes.domain.weather.entity.HourlyWeather;
import com.seungah.todayclothes.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HourlyWeatherRepository extends JpaRepository<HourlyWeather, Long> {
    HourlyWeather findByDateAndRegion(LocalDateTime date, Region region);

    @Modifying
    @Query("delete from HourlyWeather w where w.date < :beforeDate")
    void deleteHourlyWeatherBefore(@Param("beforeDate") LocalDateTime beforeDate);

}
