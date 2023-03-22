package com.seungah.todayclothes.entity;

import com.seungah.todayclothes.entity.common.BaseEntity;
import com.seungah.todayclothes.type.Region;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

	private LocalDate date;

	@Enumerated(EnumType.STRING)
	private Region region;

	private Double temp;
	private Double humidity;
	private String icon;

}
