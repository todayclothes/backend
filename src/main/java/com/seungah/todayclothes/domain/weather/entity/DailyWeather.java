package com.seungah.todayclothes.domain.weather.entity;

import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.global.common.BaseEntity;
import com.seungah.todayclothes.global.type.TimeOfDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

	private Double lowTemp;
	private Double highTemp;

	private Double windSpeed;
	private Double rain;
	private Double humidity;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	@ElementCollection
	@CollectionTable(name = "avg_temps", joinColumns = {@JoinColumn(name = "daily_weather_id")})
	@MapKeyColumn(name = "time_of_day")
	@Column(name = "avg_temp")
	private Map<TimeOfDay, Double> avgTemps = new HashMap<>();

}
