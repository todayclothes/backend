package com.seungah.todayclothes.domain.weather.controller;

import com.seungah.todayclothes.domain.weather.dto.response.DailyWeatherResponse;
import com.seungah.todayclothes.domain.weather.dto.response.HourlyWeatherResponse;
import com.seungah.todayclothes.domain.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/hourly")
    public ResponseEntity<HourlyWeatherResponse> getHourlyWeather(@AuthenticationPrincipal Long userId,
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd-HH")LocalDateTime now){
        return weatherService.getHourlyWeather(userId, now);
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyWeatherResponse> getDailyWeather(@AuthenticationPrincipal Long userId,
                                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDateTime now) {
        return weatherService.getDailyWeather(userId, now);
    }
}