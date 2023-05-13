package com.seungah.todayclothes.domain.weather.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_DAILY_WEATHER;
import static com.seungah.todayclothes.global.exception.ErrorCode.NOT_FOUND_HOURLY_WEATHER;

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

        return ResponseEntity.ok(new HourlyWeatherResponse().of(hourlyWeather));
    }
    @Transactional
    public ResponseEntity<DailyWeatherResponse> getDailyWeather(Long userId, LocalDateTime now) {
        LocalDateTime localDateTime = now.withHour(12).withMinute(0).withSecond(0).withNano(0);
        Region region = userId == null ? regionRepository.findByName("서울특별시")
                : regionRepository.findByName(memberRepository.findById(userId).get().getRegion());

        Optional<DailyWeather> dailyWeather = dailyWeatherRepository.findByDateAndRegion(localDateTime, region);
        if (dailyWeather.isEmpty()) {
            throw new CustomException(NOT_FOUND_DAILY_WEATHER);
        }
        Double highTemp = dailyWeather.get().getHighTemp();
        Double lowTemp = dailyWeather.get().getLowTemp();
        return ResponseEntity.ok(new DailyWeatherResponse().of(highTemp, lowTemp));
    }
}
