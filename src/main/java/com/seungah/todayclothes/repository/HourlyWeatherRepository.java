package com.seungah.todayclothes.repository;

import com.seungah.todayclothes.entity.HourlyWeather;
import com.seungah.todayclothes.entity.Region;
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
    @Query("delete from HourlyWeather w where w.date < :cutOffDate")
    void deleteHourlyWeatherBefore(@Param("cutOffDate") LocalDateTime cutOffDate);

}
