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
    private String icon;
    private String regionName;

    public static HourlyWeatherResponse of(HourlyWeather hourlyWeather) {
        return HourlyWeatherResponse.builder()
                .temp(hourlyWeather.getTemp())
                .icon(hourlyWeather.getIcon())
                .regionName(hourlyWeather.getRegion().getName())
                .build();
    }
}