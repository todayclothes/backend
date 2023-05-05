package com.seungah.todayclothes.domain.weather.dto.response;

import com.seungah.todayclothes.domain.weather.entity.HourlyWeather;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyWeatherResponse {
    private Double temp;
    private String regionName;
    private String description;
    private String icon;

    public static HourlyWeatherResponse of(HourlyWeather hourlyWeather) {
        return HourlyWeatherResponse.builder()
                .temp(hourlyWeather.getTemp())
                .icon(hourlyWeather.getIcon())
                .description(hourlyWeather.getDescription())
                .regionName(hourlyWeather.getRegion().getName())
                .build();
    }
}