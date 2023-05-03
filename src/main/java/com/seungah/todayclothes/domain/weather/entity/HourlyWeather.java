package com.seungah.todayclothes.domain.weather.entity;

import com.seungah.todayclothes.domain.region.entity.Region;
import com.seungah.todayclothes.global.common.BaseEntity;
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
public class HourlyWeather extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime date;

	private Double temp;
	private Double humidity;
	private String icon;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	public void from(Double temp, Double humidity, String icon) {
		this.temp = temp;
		this.humidity = humidity;
		this.icon = icon;
	}
}
