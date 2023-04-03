package com.seungah.todayclothes.entity;

import com.seungah.todayclothes.entity.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	public void update(Double temp, Double humidity, String icon){
		this.temp = temp;
		this.humidity = humidity;
		this.icon = icon;
	}
}
