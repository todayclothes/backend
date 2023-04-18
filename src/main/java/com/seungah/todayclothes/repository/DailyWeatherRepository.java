package com.seungah.todayclothes.repository;

import com.seungah.todayclothes.entity.DailyWeather;
import com.seungah.todayclothes.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DailyWeatherRepository extends JpaRepository<DailyWeather, Long> {
    List<DailyWeather> findByDateAndRegion(LocalDateTime date, Region region);
    @Modifying
    @Query("delete from DailyWeather w where w.date < :beforeDate")
    void deleteDailyWeatherBefore(@Param("beforeDate") LocalDateTime beforeDate);
}
