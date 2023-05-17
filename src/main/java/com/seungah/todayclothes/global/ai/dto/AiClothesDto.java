package com.seungah.todayclothes.global.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AiClothesDto {

	private Integer topGroup;
	private Integer bottomGroup;
	private Double avgTemp;

	public static AiClothesDto of(String topGroup, String bottomGroup, Double avgTemp) {
		return AiClothesDto.builder()
			.topGroup(Integer.parseInt(topGroup))
			.bottomGroup(Integer.parseInt(bottomGroup))
			.avgTemp(avgTemp)
			.build();
	}

}
