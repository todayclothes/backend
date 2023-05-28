package com.seungah.todayclothes.domain.region.entity;

import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Region {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;			// 지역명
	private Double latitude;		// 위도
	private Double longitude;		// 경도

	public static Region of(String name, Double latitude, Double longitude) {
		return Region.builder()
				.name(name)
				.latitude(latitude)
				.longitude(longitude)
				.build();
	}

}
