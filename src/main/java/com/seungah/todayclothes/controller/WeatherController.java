package com.seungah.todayclothes.controller;

import com.seungah.todayclothes.dto.response.DailyWeatherResponse;
import com.seungah.todayclothes.dto.response.HourlyWeatherResponse;
import com.seungah.todayclothes.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/hourly")
    public ResponseEntity<HourlyWeatherResponse> getHourlyWeather(@AuthenticationPrincipal Long userId, LocalDateTime now){
        return weatherService.getHourlyWeather(userId, now);
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyWeatherResponse> getDailyWeather(@AuthenticationPrincipal Long userId, LocalDateTime now) {
        return weatherService.getDailyWeather(userId, now);
    }
}