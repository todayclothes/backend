package com.seungah.todayclothes.domain.weather.service;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_DAILY_WEATHER;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_HOURLY_WEATHER;

import com.seungah.todayclothes.domain.member.repository.MemberRepository;
import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.region.repository.RegionRepository;
import com.seungah.todayclothes.domain.weather.dto.response.DailyWeatherResponse;
import com.seungah.todayclothes.domain.weather.dto.response.HourlyWeatherResponse;
import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import com.seungah.todayclothes.domain.weather.entity.HourlyWeather;
import com.seungah.todayclothes.domain.weather.repository.DailyWeatherRepository;
import com.seungah.todayclothes.domain.weather.repository.HourlyWeatherRepository;
import com.seungah.todayclothes.global.exception.CustomException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class WeatherService {
    private final DailyWeatherRepository dailyWeatherRepository;
    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<HourlyWeatherResponse> getHourlyWeather(Long userId, LocalDateTime now) {
        LocalDateTime localDateTime = now.withMinute(0).withSecond(0).withNano(0);
        Region region = userId == null ? regionRepository.findByName("서울특별시")
                : regionRepository.findByName(memberRepository.findById(userId).get().getRegion());
        HourlyWeather hourlyWeather = hourlyWeatherRepository.findByDateAndRegion(localDateTime, region);
        if (hourlyWeather == null){
            throw new CustomException(NOT_FOUND_HOURLY_WEATHER);
        }

        return ResponseEntity.ok(HourlyWeatherResponse.of(hourlyWeather));
    }
    @Transactional
    public ResponseEntity<DailyWeatherResponse> getDailyWeather(Long userId, LocalDate now) {
        LocalDateTime localDateTime = now.atTime(0, 0);
        Region region = userId == null ? regionRepository.findByName("서울특별시")
                : regionRepository.findByName(memberRepository.findById(userId).get().getRegion());

        DailyWeather dailyWeather = dailyWeatherRepository.findByDateAndRegion(localDateTime, region)
            .orElseThrow(() -> new CustomException(NOT_FOUND_DAILY_WEATHER));

        return ResponseEntity.ok(DailyWeatherResponse.of(dailyWeather));
    }
}
