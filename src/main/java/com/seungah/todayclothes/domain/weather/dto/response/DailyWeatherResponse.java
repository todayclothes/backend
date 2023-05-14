package com.seungah.todayclothes.domain.weather.dto.response;

import com.seungah.todayclothes.domain.weather.entity.DailyWeather;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyWeatherResponse {
    private Long id;
    private Double highestTemp;
    private Double lowestTemp;

    public static DailyWeatherResponse of(DailyWeather dailyWeather) {
        return DailyWeatherResponse.builder()
                .id(dailyWeather.getId())
                .highestTemp(dailyWeather.getHighTemp())
                .lowestTemp(dailyWeather.getLowTemp())
                .build();
    }
}