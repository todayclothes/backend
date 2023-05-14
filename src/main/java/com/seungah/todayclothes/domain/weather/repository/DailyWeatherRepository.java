package com.seungah.todayclothes.domain.weather.repository;

import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DailyWeatherRepository extends JpaRepository<DailyWeather, Long> {

    @Query("SELECT dw FROM DailyWeather dw WHERE FUNCTION('DATE_FORMAT', dw.date, '%Y-%m-%d') = FUNCTION('DATE_FORMAT', :date, '%Y-%m-%d') AND dw.region = :region")
    Optional<DailyWeather> findByDateAndRegion(LocalDateTime date, Region region);
    @Modifying
    @Query("delete from DailyWeather w where w.date < :beforeDate")
    void deleteDailyWeatherBefore(@Param("beforeDate") LocalDateTime beforeDate);
}
