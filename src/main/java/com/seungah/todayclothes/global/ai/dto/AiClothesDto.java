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

	public static AiClothesDto of(String topGroup, String bottomGroup) {
		return AiClothesDto.builder()
			.topGroup(Integer.parseInt(topGroup))
			.bottomGroup(Integer.parseInt(bottomGroup))
			.build();
	}

}
