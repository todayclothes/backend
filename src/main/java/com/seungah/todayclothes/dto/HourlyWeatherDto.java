package com.seungah.todayclothes.dto;


import com.seungah.todayclothes.entity.HourlyWeather;
import com.seungah.todayclothes.entity.Region;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public class HourlyWeatherDto {
    private LocalDateTime date;
    private Double temp;
    private Double humidity;
    private String icon;
    private Region region;

    public HourlyWeather toEntity(){
        return HourlyWeather.builder()
                .date(date)
                .temp(temp)
                .humidity(humidity)
                .icon(icon)
                .region(region)
                .build();
    }
}
