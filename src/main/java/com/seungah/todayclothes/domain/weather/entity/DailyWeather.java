package com.seungah.todayclothes.domain.weather.entity;

import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.domain.scheduleweather.entity.ScheduleWeather;
import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.type.TimeOfDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DailyWeather extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime date;

	private Double avgTemp;
	private Double windSpeed;
	private Double rain;
	private Double humidity;
	private String icon;
	private TimeOfDay timeOfDay;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@ManyToOne
	@JoinColumn(name = "schedule_weather_id")
	private ScheduleWeather scheduleWeather;

	public void from(Double avgTemp, Double windSpeed, Double rain, Double humidity, String icon) {
		this.avgTemp = avgTemp;
		this.windSpeed = windSpeed;
		this.rain = rain;
		this.humidity = humidity;
		this.icon = icon;
	}
}
