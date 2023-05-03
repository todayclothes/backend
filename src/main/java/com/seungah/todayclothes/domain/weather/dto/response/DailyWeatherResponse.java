package com.seungah.todayclothes.domain.weather.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyWeatherResponse {
    private Double highestTemp;
    private Double lowestTemp;

    public static DailyWeatherResponse of(Double highestTemp, Double lowestTemp) {
        return DailyWeatherResponse.builder()
                .highestTemp(highestTemp)
                .lowestTemp(lowestTemp)
                .build();
    }
}